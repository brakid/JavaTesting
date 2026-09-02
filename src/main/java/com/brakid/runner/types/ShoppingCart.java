package com.brakid.runner.types;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ShoppingCart {
    private final long id;
    private final User user;
    @Getter(AccessLevel.NONE)
    private final Map<Long, Integer> products;

    @JsonCreator
    public ShoppingCart(
            @JsonProperty("id") long id,
            @JsonProperty("user") User user,
            @JsonProperty("products") Map<Long, Integer> products) {
        this.id = id;
        this.user = user;
        this.products = products;
    }

    public ImmutableMap<Long, Integer> getProducts() {
        return ImmutableMap.copyOf(products);
    }

    public ImmutableMap<Product, Integer> getResolvedProducts(List<Product> products) {
        ImmutableMap<Long, Product> productsById = Maps.uniqueIndex(products, product -> product.id());
        return ImmutableMap.copyOf(
                Maps.transformValues(
                        Maps.uniqueIndex(
                                this.products.entrySet().stream()
                                        .map(entry -> new Pair<>(Optional.fromNullable(productsById.get(entry.getKey())), entry.getValue()))
                                        .filter(pair -> pair.key().isPresent())
                                        .map(pair -> new Pair<>(pair.key().get(), pair.value()))
                                        .toList(),
                                pair -> pair.key()),
                        pair -> pair.value()));
    }

    public void addProduct(Product product, int quantity) {
        this.products.put(product.id(), this.products.getOrDefault(product.id(), 0) + quantity);
    }
}