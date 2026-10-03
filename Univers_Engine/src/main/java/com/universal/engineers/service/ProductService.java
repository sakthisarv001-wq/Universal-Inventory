package com.universal.engineers.service;

import com.universal.engineers.model.Product;
import com.universal.engineers.model.ProductStatus;
import com.universal.engineers.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repo;
    private final CatalogService catalog;

    public ProductService(ProductRepository repo, CatalogService catalog) {
        this.repo = repo;
        this.catalog = catalog;
    }

    public List<Product> all() { return repo.findAll(); }

    public Product get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    public Product create(Product p, MultipartFile image) throws IOException {
        normalizeAndValidate(p);
        p.setProductCode(uniqueCode());
        p.setCurrentStock(p.getOpeningStock());
        if (p.getStatus() == null) p.setStatus(ProductStatus.ACTIVE);
        applyImage(p, image);
        return repo.save(p);
    }

    @Transactional
    public Product update(Long id, Product p, MultipartFile image) throws IOException {
        Product old = get(id);
        normalizeAndValidate(p);

        // Every editable product field is copied to the existing DB row.
        // The product ID remains unchanged because it is the primary key.
        if (p.getProductCode() == null || p.getProductCode().isBlank()) {
            throw new IllegalArgumentException("Product code is required");
        }

        Optional<Product> codeOwner = repo.findByProductCode(p.getProductCode());
        if (codeOwner.isPresent() && !codeOwner.get().getId().equals(id)) {
            throw new IllegalArgumentException("Product code already exists: " + p.getProductCode());
        }

        old.setProductCode(p.getProductCode());
        old.setProductName(p.getProductName());
        old.setCategory(p.getCategory());
        old.setSubCategory(p.getSubCategory());
        old.setUnitOfMeasurement(p.getUnitOfMeasurement());
        old.setBrand(p.getBrand());
        old.setSpecificProductDetails(p.getSpecificProductDetails());
        old.setOpeningStock(p.getOpeningStock());
        old.setCurrentStock(p.getCurrentStock());
        old.setPurchaseRate(p.getPurchaseRate());
        old.setSupplierVendor(p.getSupplierVendor());
        old.setLocationRack(p.getLocationRack());
        old.setGstTax(p.getGstTax());
        old.setGstPercentage(p.getGstPercentage());
        old.setHsnCode(p.getHsnCode());
        old.setGstInvoiceId(p.getGstInvoiceId());
        old.setStatus(p.getStatus() == null ? ProductStatus.ACTIVE : p.getStatus());

        // If no new image is selected, the existing image remains unchanged.
        applyImage(old, image);
        return repo.save(old);
    }

    @Transactional
    public void issueStock(Long productId, BigDecimal quantity) {
        if (quantity == null || quantity.signum() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        Product p = repo.findByIdForUpdate(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
        if (p.getCurrentStock().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Requested quantity is greater than current stock (" + p.getCurrentStock() + " " + p.getUnitOfMeasurement() + ")");
        }
        p.setCurrentStock(p.getCurrentStock().subtract(quantity));
        repo.save(p);
    }

    @Transactional
    public void delete(Long id) {
        Product p = get(id);
        // Database FK is configured with ON DELETE CASCADE for product_requests.
        // Therefore deleting this product also permanently removes its request rows.
        repo.delete(p);
        repo.flush();

        if (repo.existsById(id)) {
            throw new IllegalStateException("Product was not deleted");
        }
    }

    public List<Product> search(String q, String category, String subCategory) {
        if (q != null && !q.isBlank()) {
            return repo.findAll().stream()
                    .filter(p -> contains(p.getProductName(), q)
                            || contains(p.getProductCode(), q)
                            || contains(p.getCategory(), q)
                            || contains(p.getSubCategory(), q)
                            || contains(p.getBrand(), q))
                    .toList();
        }
        if (category != null && !category.isBlank() && subCategory != null && !subCategory.isBlank()) {
            return repo.findByCategoryAndSubCategory(category, subCategory);
        }
        if (category != null && !category.isBlank()) return repo.findByCategory(category);
        return all();
    }

    private boolean contains(String s, String q) {
        return s != null && s.toLowerCase().contains(q.toLowerCase());
    }

    private void normalizeAndValidate(Product p) {
        if (p.getProductName() == null || p.getProductName().isBlank())
            throw new IllegalArgumentException("Product name is required");
        if (!catalog.valid(p.getCategory(), p.getSubCategory()))
            throw new IllegalArgumentException("Invalid category / sub category");
        if (p.getUnitOfMeasurement() == null || p.getUnitOfMeasurement().isBlank())
            throw new IllegalArgumentException("Unit is required");
        if (p.getOpeningStock() == null || p.getOpeningStock().signum() < 0)
            throw new IllegalArgumentException("Opening stock must be 0 or more");
        if (p.getCurrentStock() != null && p.getCurrentStock().signum() < 0)
            throw new IllegalArgumentException("Current stock must be 0 or more");
        if (p.getPurchaseRate() != null && p.getPurchaseRate().signum() < 0)
            throw new IllegalArgumentException("Purchase rate cannot be negative");
        if (p.getGstPercentage() == null) p.setGstPercentage(BigDecimal.ZERO);
        if (p.getGstPercentage().signum() < 0 || p.getGstPercentage().compareTo(BigDecimal.valueOf(100)) > 0)
            throw new IllegalArgumentException("GST percentage must be between 0 and 100");
    }

    private void applyImage(Product p, MultipartFile f) throws IOException {
        if (f != null && !f.isEmpty()) {
            if (f.getContentType() == null || !f.getContentType().startsWith("image/"))
                throw new IllegalArgumentException("Please upload an image file");
            p.setImageData(f.getBytes());
            p.setImageContentType(f.getContentType());
            p.setImageFileName(f.getOriginalFilename());
        }
    }

    private String uniqueCode() {
        String code;
        do {
            code = "UE-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (repo.findByProductCode(code).isPresent());
        return code;
    }
}
