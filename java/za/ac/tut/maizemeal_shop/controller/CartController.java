package za.ac.tut.maizemeal_shop.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.ac.tut.maizemeal_shop.model.Order;
import za.ac.tut.maizemeal_shop.model.OrderProduct;
import za.ac.tut.maizemeal_shop.model.OrderStatus;
import za.ac.tut.maizemeal_shop.model.Product;
import za.ac.tut.maizemeal_shop.model.ShoppingCartService;
import za.ac.tut.maizemeal_shop.model.User;
import za.ac.tut.maizemeal_shop.repository.OrderProductRepository;
import za.ac.tut.maizemeal_shop.repository.OrderRepository;
import za.ac.tut.maizemeal_shop.repository.ProductRepository;
import za.ac.tut.maizemeal_shop.repository.UserRepository;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository; // Assuming you have repositories for Order and OrderProduct

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")
    public List<Product> viewCart() {
        List<Product> cartProducts = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : shoppingCartService.getCart().entrySet()) {
            Optional<Product> productOptional = productRepository.findById(entry.getKey());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setStockQuantity(entry.getValue());
                cartProducts.add(product);
            }
        }
        return cartProducts;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            shoppingCartService.addToCart(productId);
            return ResponseEntity.ok("Product added to cart successfully.");
        } else {
            return ResponseEntity.badRequest().body("Product not found.");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long productId) {
        if (productRepository.existsById(productId)) {
            shoppingCartService.removeFromCart(productId);
            return ResponseEntity.ok("Product removed from cart successfully.");
        } else {
            return ResponseEntity.badRequest().body("Product not found in the cart.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCart(@RequestParam Long productId, @RequestParam Integer quantity) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            shoppingCartService.updateCart(productId, quantity);
            return ResponseEntity.ok("Cart updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Product not found.");
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart(@RequestParam("totalAmount") double totalAmount,
            @RequestParam("userEmail") String userEmail) {
        // Ensure the user is logged in
        if (userEmail == null || userEmail.isEmpty()) {
            // Handle case when user is not logged in
            return ResponseEntity.ok("Please Login successfully.");
        }

        // Find the user by email
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            // Handle case when user does not exist
            return ResponseEntity.ok("User does not exist.");
        }

        // Create a new Order
        Order order = new Order();
        order.setTotalPrice(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        order.setUser(user); // Set the user directly

        // Save the order
        orderRepository.save(order);

        // Get cart items
        Map<Long, Integer> cartItems = shoppingCartService.getCart();

        // Iterate over cart items and create OrderProduct entries
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                // Create OrderProduct
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setQuantity(quantity);

                // Save OrderProduct
                orderProductRepository.save(orderProduct);
            }
        }

        // Clear the cart after successful checkout
        shoppingCartService.clearCart();

        return ResponseEntity.ok("Checkout successful.");
    }
}