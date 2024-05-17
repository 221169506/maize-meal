package za.ac.tut.maizemeal_shop.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import za.ac.tut.maizemeal_shop.exception.ResourceNotFoundException;
import za.ac.tut.maizemeal_shop.model.Product;
import za.ac.tut.maizemeal_shop.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        // For each product, retrieve the image data and convert it to a base64-encoded
        // string
        products.forEach(product -> {
            byte[] imageData = product.getImage();
            if (imageData != null && imageData.length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(imageData);
                product.setBase64Image(base64Image);
            }
        });

        return products;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestParam("image") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("size") String size,
            @RequestParam("stockQuantity") int stockQuantity,
            @RequestParam("price") double price) {
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload an image file");
            }

            // Save image to database or filesystem
            byte[] imageData = file.getBytes();
            // Save other product details to database
            Product product = new Product(name, description, size, stockQuantity, price, imageData);
            productRepository.save(product);

            return ResponseEntity.ok("Product added successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
            @RequestParam("image") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("size") String size,
            @RequestParam("stockQuantity") int stockQuantity,
            @RequestParam("price") double price) {
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload an image file");
            }

            // Save image to database or filesystem
            byte[] imageData = file.getBytes();

            // Retrieve the existing product from the database
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

            // Update the product details
            product.setName(name);
            product.setDescription(description);
            product.setSize(size);
            product.setStockQuantity(stockQuantity);
            product.setPrice(price);
            product.setImage(imageData);

            // Save the updated product
            productRepository.save(product);

            return ResponseEntity.ok("Product updated successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        productRepository.delete(product);

        return ResponseEntity.ok().build();
    }

    // Get total count of products
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProductsCount() {
        try {
            long count = productRepository.count();
            System.out.println(count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Search products by keyword (name, size, description)
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable String keyword) {
        try {
            // Search for products by keyword in name, size, and description
            List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
            products.addAll(productRepository.findBySizeContainingIgnoreCase(keyword));
            products.addAll(productRepository.findByDescriptionContainingIgnoreCase(keyword));

            // Return the list of matching products
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
