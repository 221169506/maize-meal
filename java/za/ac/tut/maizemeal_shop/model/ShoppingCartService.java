package za.ac.tut.maizemeal_shop.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class ShoppingCartService {

    private Map<Long, Integer> cart = new HashMap<>();

    public void addToCart(Long productId) {
        
        cart.put(productId, cart.getOrDefault(productId, 0) + 1);
    }

    public void removeFromCart(Long productId) {
        cart.remove(productId);
    }

    public void updateCart(Long productId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(productId);
        } else {
            cart.put(productId, quantity);
        }
    }

    public Map<Long, Integer> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}
