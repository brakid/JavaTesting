package com.brakid.runner.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.brakid.runner.types.Product;
import com.brakid.runner.types.ShoppingCart;
import com.brakid.runner.types.User;
import static com.brakid.runner.utils.Utils.JSON_MAPPER;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import lombok.Value;

@Value
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Database {
    private final List<User> users;
    private final List<Product> products;
    private final List<ShoppingCart> shoppingCarts;

    @JsonCreator
    public Database(
            @JsonProperty("users") List<User> users,
            @JsonProperty("products") List<Product> products,
            @JsonProperty("shoppingCarts") List<ShoppingCart> shoppingCarts) {
        this.users = users;
        this.products = products;
        this.shoppingCarts = shoppingCarts;
    }

    public static Database init() {
        return new Database(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

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
        ShoppingCart shoppingCart = new ShoppingCart(shoppingCarts.size(), user, new HashMap<>());
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
