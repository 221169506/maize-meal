package za.ac.tut.maizemeal_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.ac.tut.maizemeal_shop.model.ShoppingCart;

@Configuration
public class AppConfig {

    @Bean
    public ShoppingCart shoppingCart() {
        return new ShoppingCart();
    }
}
