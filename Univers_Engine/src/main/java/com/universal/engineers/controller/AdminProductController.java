package com.universal.engineers.controller;
import com.universal.engineers.model.*;
import com.universal.engineers.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Controller @RequestMapping("/admin") public class AdminProductController{
 private final ProductService products;private final CatalogService catalog;public AdminProductController(ProductService products,CatalogService catalog){this.products=products;this.catalog=catalog;}
 private void common(Model m){m.addAttribute("categories",catalog.getCategories());m.addAttribute("units",List.of("Nos","Kg","Ton","Meter","Liter","Sheet","Box","Set"));m.addAttribute("statuses",ProductStatus.values());}
 @GetMapping("/products") public String manage(Model m){m.addAttribute("products",products.all());return "admin-products";}
 @GetMapping("/products/create") public String create(Model m){m.addAttribute("product",new Product());common(m);m.addAttribute("subCategories",List.of());return "admin-create-product";}
 @PostMapping("/products/create") public String create(@ModelAttribute Product p,@RequestParam(required=false) MultipartFile image,Model m){try{products.create(p,image);return "redirect:/admin/products?created=true";}catch(Exception e){m.addAttribute("error",e.getMessage());m.addAttribute("product",p);common(m);m.addAttribute("subCategories",catalog.getSubCategories(p.getCategory()));return "admin-create-product";}}
 @GetMapping("/products/edit/{id}") public String edit(@PathVariable Long id,Model m){Product p=products.get(id);m.addAttribute("product",p);common(m);m.addAttribute("subCategories",catalog.getSubCategories(p.getCategory()));m.addAttribute("allowProductCodeEdit",true);return "admin-update-product";}
 @PostMapping("/products/edit/{id}") public String edit(@PathVariable Long id,@ModelAttribute Product p,@RequestParam(required=false) MultipartFile image){try{products.update(id,p,image);return "redirect:/admin/products?updated=true";}catch(Exception e){return "redirect:/admin/products?error="+java.net.URLEncoder.encode(e.getMessage(),java.nio.charset.StandardCharsets.UTF_8);}}
 @GetMapping("/products/delete") public String deletePage(Model m){m.addAttribute("products",products.all());return "admin-delete-product";}
 @PostMapping("/products/delete")
 public String delete(@RequestParam Long id){
     try {
         products.delete(id);
         return "redirect:/admin/products?deleted=true";
     } catch(Exception e) {
         return "redirect:/admin/products?deleteError=true";
     }
 }

}
