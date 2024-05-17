package za.ac.tut.maizemeal_shop.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public ShoppingCart(List<Product> items) {
        this.items = items;
    }

    // Add a product to the cart
    public void addItem(Product product) {
        items.add(product);
    }

    // Remove a product from the cart
    public void removeItem(Product product) {
        items.remove(product);
    }

    // Get all items in the cart
    public List<Product> getItems() {
        return items;
    }

    // Clear the cart
    public void clearCart() {
        items.clear();
    }

    // Calculate total price of all items in the cart
    public double calculateTotal() {
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }
        return total;
    }
}