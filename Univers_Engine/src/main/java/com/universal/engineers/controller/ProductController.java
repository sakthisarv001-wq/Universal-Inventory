package com.universal.engineers.controller;
import com.universal.engineers.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller @RequestMapping("/products") public class ProductController{
 private final ProductService products;private final CatalogService catalog;public ProductController(ProductService products,CatalogService catalog){this.products=products;this.catalog=catalog;}
 @GetMapping public String products(@RequestParam(required=false)String q,@RequestParam(required=false)String category,@RequestParam(required=false)String subCategory,Model model){model.addAttribute("products",products.search(q,category,subCategory));model.addAttribute("categories",catalog.getCategories());model.addAttribute("subCategories",category==null?java.util.List.of():catalog.getSubCategories(category));model.addAttribute("q",q);model.addAttribute("selectedCategory",category);model.addAttribute("selectedSubCategory",subCategory);return "products";}
 @GetMapping("/{id}") public String detail(@PathVariable Long id,Model model){model.addAttribute("product",products.get(id));model.addAttribute("units",java.util.List.of("Nos","Kg","Ton","Meter","Liter","Sheet","Box","Set"));return "product-detail";}
}
