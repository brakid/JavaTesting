package com.brakid.runner.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ShoppingCart {
    @Setter(AccessLevel.NONE)
    private long id;
    @Setter(AccessLevel.NONE)
    private User user;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Map<Long, Integer> products;

    public ShoppingCart(long id, User user) {
        this.id = id;
        this.user = user;
        this.products = new HashMap<>();
    }

    public ImmutableMap<Product, Integer> getProducts(List<Product> products) {
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
