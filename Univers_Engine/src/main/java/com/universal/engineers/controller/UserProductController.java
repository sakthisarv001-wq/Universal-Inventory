package com.universal.engineers.controller;

import com.universal.engineers.service.ProductRequestService;
import com.universal.engineers.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/user")
public class UserProductController {
    private final ProductService products;
    private final ProductRequestService requests;

    public UserProductController(ProductService products, ProductRequestService requests) {
        this.products = products;
        this.requests = requests;
    }

    @GetMapping("/request/{id}")
    public String request(@PathVariable Long id, Model m) {
        m.addAttribute("product", products.get(id));
        return "user-request";
    }

    @PostMapping("/request/{id}")
    public String request(@PathVariable Long id,
                          @RequestParam String uom,
                          @RequestParam BigDecimal quantity,
                          Authentication auth,
                          Model m) {
        try {
            requests.create(auth.getName(), id, uom, quantity);
            return "redirect:/user/requests?success=true";
        } catch (Exception e) {
            m.addAttribute("product", products.get(id));
            m.addAttribute("error", e.getMessage());
            return "user-request";
        }
    }

    @GetMapping("/requests")
    public String mine(Authentication auth, Model m) {
        m.addAttribute("requests", requests.mine(auth.getName()));
        return "user-requests";
    }
}
