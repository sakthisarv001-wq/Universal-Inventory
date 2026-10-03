package com.universal.engineers.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/after-login")
    public String afterLogin(Authentication a) {
        boolean admin = a.getAuthorities().stream()
                .anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"));
        return admin ? "redirect:/admin/products" : "redirect:/products";
    }
}
