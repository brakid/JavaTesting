package com.brakid.runner.storage;

import java.util.ArrayList;
import java.util.List;

import com.brakid.runner.types.Product;
import com.brakid.runner.types.ShoppingCart;
import com.brakid.runner.types.User;
import static com.brakid.runner.utils.Utils.JSON_MAPPER;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.common.collect.ImmutableList;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Database {
    @Setter(AccessLevel.NONE)
    private List<User> users = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    private List<Product> products = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();

    public static Database load(String json) {
        return JSON_MAPPER.readValue(json, Database.class);
    }

    public String store() {
        return JSON_MAPPER.writeValueAsString(this);
    }

    public User createUser(String name, String emailAddress) {
        User user = new User(users.size(), name, emailAddress);
        users.add(user);
        return user;
    }

    public Product createProduct(String name, float price) {
        Product product = new Product(products.size(), name, price);
        products.add(product);
        return product;
    }

    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart(shoppingCarts.size(), user);
        shoppingCarts.add(shoppingCart);
        return shoppingCart;
    }

    public ImmutableList<ShoppingCart> getShoppingCard(User user) {
        return ImmutableList.copyOf(
                this.shoppingCarts.stream()
                        .filter(shoppingCart -> shoppingCart.getUser().equals(user))
                        .toList());
    }
}
