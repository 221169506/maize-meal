package za.ac.tut.maizemeal_shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.ac.tut.maizemeal_shop.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    // You can add custom query methods here if needed
}