package com.universal.engineers.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public String notFound(NoSuchElementException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", ex.getMessage() == null ? "The requested record was not found." : ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String badRequest(IllegalArgumentException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("message", ex.getMessage() == null ? "Please check the submitted details." : ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String unexpected(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("message", "Something went wrong while processing the request. Please return and try again.");
        return "error";
    }
}
