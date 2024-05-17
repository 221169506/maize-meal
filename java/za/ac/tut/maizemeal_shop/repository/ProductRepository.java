package za.ac.tut.maizemeal_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.ac.tut.maizemeal_shop.model.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
   
        // Custom method to search products by name using the %Like operator
        List<Product> findByNameContainingIgnoreCase(String keyword);

        // Custom method to search products by size using the %Like operator
        List<Product> findBySizeContainingIgnoreCase(String keyword);
    
        // Custom method to search products by description using the %Like operator
        List<Product> findByDescriptionContainingIgnoreCase(String keyword);
    
}
