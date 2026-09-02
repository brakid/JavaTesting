package com.brakid.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brakid.runner.storage.Database;
import com.brakid.runner.types.Product;
import com.brakid.runner.types.ShoppingCart;
import com.brakid.runner.types.User;
import static com.brakid.runner.utils.Utils.JSON_MAPPER;

public class Main {
    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        final Database database = Database.init();
        final User user1 = database.createUser("Test", "test@test.com");
        final User user2 = database.createUser("Test2", "me@test.com");

        final Product product1 = database.createProduct("Earplugs", 1.23f);
        final Product product2 = database.createProduct("Sunglasses", 10.99f);

        final ShoppingCart shoppingCart1 = database.createShoppingCart(user1);
        final ShoppingCart shoppingCart2 = database.createShoppingCart(user1);
        final ShoppingCart shoppingCart3 = database.createShoppingCart(user2);

        shoppingCart1.addProduct(product1, 1);
        shoppingCart2.addProduct(product2, 1);
        shoppingCart3.addProduct(product1, 1);
        shoppingCart3.addProduct(product1, 1);
        shoppingCart3.addProduct(product2, 2);

        String json = JSON_MAPPER.writeValueAsString(database);
        LOGGER.info("JSON Serialized: {}", json);

        final Database database2 = Database.load(json);
        ShoppingCart cart = database2.getShoppingCard(user2).getFirst();
        LOGGER.info("Equals? {}", shoppingCart3.equals(cart));
        LOGGER.info("Shopping cart for user2: {}", cart);
        LOGGER.info("Resolved: {}", cart.getResolvedProducts(database.getProducts()));

        /*ExecutorService executor = newVirtualThreadPerTaskExecutor();
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
        executor.shutdown();*/
    }
}