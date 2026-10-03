package com.universal.engineers.config;

import com.universal.engineers.model.AppUser;
import com.universal.engineers.model.Product;
import com.universal.engineers.model.Role;
import com.universal.engineers.repository.AppUserRepository;
import com.universal.engineers.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(AppUserRepository users, ProductRepository products, PasswordEncoder encoder) {
        return args -> {
            saveUser(users, encoder, "admin", "admin123", Role.ADMIN);
            saveUser(users, encoder, "user", "user123", Role.USER);

            // Backward-compatible migration for databases created by the earlier ZIP.
            for (Product p : products.findAll()) {
                if (p.getCurrentStock() == null) p.setCurrentStock(p.getOpeningStock());
                if (p.getStatus() == null) {
                    p.setStatus(com.universal.engineers.model.ProductStatus.ACTIVE);
                }
                products.save(p);
            }
        };
    }

    private void saveUser(AppUserRepository repo, PasswordEncoder encoder,
                          String username, String password, Role role) {
        repo.findByUsername(username).ifPresentOrElse(u -> {
            if (!encoder.matches(password, u.getPassword()) || u.getRole() != role) {
                u.setPassword(encoder.encode(password));
                u.setRole(role);
                repo.save(u);
            }
        }, () -> repo.save(new AppUser(username, encoder.encode(password), role)));
    }
}
