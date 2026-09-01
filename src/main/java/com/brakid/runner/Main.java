package com.brakid.runner;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brakid.runner.types.User;
import static com.brakid.runner.utils.Utils.JSON_MAPPER;
import com.google.common.collect.ImmutableList;

import tools.jackson.core.type.TypeReference;

public class Main {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws InterruptedException {
        ImmutableList<User> values = ImmutableList.of(new User(1l, "test@test.com"));
        LOGGER.info("ToString(): {}", values);

        String json = JSON_MAPPER.writeValueAsString(values);
        LOGGER.info("JSON Serialized: {}", json);

        ImmutableList<User> deserializedUsers = JSON_MAPPER.readValue(json, new TypeReference<ImmutableList<User>>() {});
        LOGGER.info("Deserialized: {}", deserializedUsers);

        ExecutorService executor = newVirtualThreadPerTaskExecutor();
        CountDownLatch latch = new CountDownLatch(10);
        ImmutableList<Future<Optional<Integer>>> futures = 
                ImmutableList.copyOf(
                        IntStream.range(0, 10)
                                .<Callable<Optional<Integer>>>mapToObj(v -> {
                                    return () -> {
                                        try {
                                            for (int i = 0; i < 10*v; i++) { 
                                                LOGGER.debug("Alive: {}", v);
                                                Thread.sleep(1000);
                                            }
                                            return Optional.of(v);
                                        } catch (InterruptedException ex) {
                                            LOGGER.error("Exception ({}): {}", v, ex);
                                            return Optional.empty();
                                        } finally {
                                            latch.countDown();
                                        }
                                    };
                                })
                                .map(executor::submit)
                                .toList());

        LOGGER.info(
                "Result: {}",  
                ImmutableList.copyOf(
                        futures.parallelStream()
                                .map(future -> {
                                    try {
                                        return future.get(); 
                                    } catch (InterruptedException | ExecutionException ex) {
                                        LOGGER.error("Exception: {}", ex);
                                        return Optional.empty();
                                    }})
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .toList()));

        latch.await(2, TimeUnit.MINUTES);
        executor.shutdown();
    }
}