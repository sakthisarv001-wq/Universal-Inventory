package com.universal.engineers.controller;
import com.universal.engineers.model.Product;
import com.universal.engineers.service.ProductService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller public class ImageController{
 private final ProductService service; public ImageController(ProductService service){this.service=service;}
 @GetMapping("/products/{id}/image") @ResponseBody public ResponseEntity<byte[]> image(@PathVariable Long id){Product p=service.get(id);if(p.getImageData()==null)return ResponseEntity.notFound().build();MediaType mt;try{mt=MediaType.parseMediaType(p.getImageContentType()==null?"image/jpeg":p.getImageContentType());}catch(Exception e){mt=MediaType.IMAGE_JPEG;}return ResponseEntity.ok().contentType(mt).body(p.getImageData());}
}
