package com.universal.engineers.service;

import com.universal.engineers.model.*;
import com.universal.engineers.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductRequestService {
    private final ProductRequestRepository requests;
    private final AppUserRepository users;
    private final ProductRepository products;
    private final ProductService productService;

    public ProductRequestService(ProductRequestRepository requests,
                                  AppUserRepository users,
                                  ProductRepository products,
                                  ProductService productService) {
        this.requests = requests;
        this.users = users;
        this.products = products;
        this.productService = productService;
    }

    @Transactional
    public void create(String username, Long productId, String uom, BigDecimal qty) {
        AppUser u = users.findByUsername(username).orElseThrow();
        Product p = products.findById(productId).orElseThrow();
        if (qty == null || qty.signum() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (!p.getUnitOfMeasurement().equalsIgnoreCase(uom)) {
            throw new IllegalArgumentException("Please use the product's unit: " + p.getUnitOfMeasurement());
        }
        if (p.getCurrentStock().compareTo(qty) < 0) {
            throw new IllegalArgumentException("Only " + p.getCurrentStock() + " " + p.getUnitOfMeasurement() + " is currently available.");
        }

        productService.issueStock(productId, qty);

        ProductRequest r = new ProductRequest();
        r.setUser(u);
        r.setProduct(p);
        r.setRequestedUom(p.getUnitOfMeasurement());
        r.setRequiredQuantity(qty);
        r.setStatus(RequestStatus.APPROVED);
        requests.save(r);
    }

    public List<ProductRequest> mine(String username) {
        return users.findByUsername(username)
                .map(requests::findByUserOrderByRequestedAtDesc)
                .orElse(List.of());
    }
}
