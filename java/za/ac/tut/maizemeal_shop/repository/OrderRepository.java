package za.ac.tut.maizemeal_shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import za.ac.tut.maizemeal_shop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT u.username, o.createdAt, o.status, o.totalPrice,o.orderId " +
            "FROM Order o JOIN o.user u " +
            "WHERE u.email = :email")
    List<Object[]> findOrderDetailsByEmail(String email);
}
