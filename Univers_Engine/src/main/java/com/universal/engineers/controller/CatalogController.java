package com.universal.engineers.controller;
import com.universal.engineers.service.CatalogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;
@Controller
public class CatalogController {
 private final CatalogService catalog;
 public CatalogController(CatalogService catalog){this.catalog=catalog;}
 @GetMapping("/api/subcategories") @ResponseBody public List<String> subcategories(@RequestParam String category){return catalog.getSubCategories(category);}
}
