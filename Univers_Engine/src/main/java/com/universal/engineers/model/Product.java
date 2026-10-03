package com.universal.engineers.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name="products",
    indexes={
        @Index(name="idx_product_name", columnList="product_name"),
        @Index(name="idx_category", columnList="category"),
        @Index(name="idx_sub_category", columnList="sub_category")
    })
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_code", nullable=false, unique=true, length=40)
    private String productCode;

    @Column(name="product_name", nullable=false, length=160)
    private String productName;

    @Column(nullable=false, length=100)
    private String category;

    @Column(name="sub_category", nullable=false, length=120)
    private String subCategory;

    @Column(name="unit_of_measurement", nullable=false, length=30)
    private String unitOfMeasurement;

    @Column(length=100)
    private String brand;

    @Column(name="specific_product_details", columnDefinition="TEXT")
    private String specificProductDetails;

    @Column(name="opening_stock", nullable=false, precision=18, scale=3)
    private BigDecimal openingStock = BigDecimal.ZERO;

    @Column(name="current_stock", nullable=false, precision=18, scale=3)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name="purchase_rate", precision=18, scale=2)
    private BigDecimal purchaseRate;

    @Column(name="supplier_vendor", length=160)
    private String supplierVendor;

    @Column(name="location_rack", length=120)
    private String locationRack;

    @Column(name="gst_tax", length=80)
    private String gstTax;

    @Column(name="gst_percentage", precision=5, scale=2)
    private BigDecimal gstPercentage = BigDecimal.ZERO;

    @Column(name="hsn_code", length=30)
    private String hsnCode;

    @Column(name="gst_invoice_id", length=100)
    private String gstInvoiceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Lob
    @Column(name="image_data", columnDefinition="LONGBLOB")
    private byte[] imageData;

    @Column(name="image_content_type", length=100)
    private String imageContentType;

    @Column(name="image_file_name", length=255)
    private String imageFileName;

    public Long getId(){return id;}
    public String getProductCode(){return productCode;}
    public void setProductCode(String v){productCode=v;}
    public String getProductName(){return productName;}
    public void setProductName(String v){productName=v;}
    public String getCategory(){return category;}
    public void setCategory(String v){category=v;}
    public String getSubCategory(){return subCategory;}
    public void setSubCategory(String v){subCategory=v;}
    public String getUnitOfMeasurement(){return unitOfMeasurement;}
    public void setUnitOfMeasurement(String v){unitOfMeasurement=v;}
    public String getBrand(){return brand;}
    public void setBrand(String v){brand=v;}
    public String getSpecificProductDetails(){return specificProductDetails;}
    public void setSpecificProductDetails(String v){specificProductDetails=v;}
    public BigDecimal getOpeningStock(){return openingStock;}
    public void setOpeningStock(BigDecimal v){openingStock=v;}
    public BigDecimal getCurrentStock(){return currentStock;}
    public void setCurrentStock(BigDecimal v){currentStock=v;}
    public BigDecimal getPurchaseRate(){return purchaseRate;}
    public void setPurchaseRate(BigDecimal v){purchaseRate=v;}
    public String getSupplierVendor(){return supplierVendor;}
    public void setSupplierVendor(String v){supplierVendor=v;}
    public String getLocationRack(){return locationRack;}
    public void setLocationRack(String v){locationRack=v;}
    public String getGstTax(){return gstTax;}
    public void setGstTax(String v){gstTax=v;}
    public BigDecimal getGstPercentage(){return gstPercentage;}
    public void setGstPercentage(BigDecimal v){gstPercentage=v;}
    public String getHsnCode(){return hsnCode;}
    public void setHsnCode(String v){hsnCode=v;}
    public String getGstInvoiceId(){return gstInvoiceId;}
    public void setGstInvoiceId(String v){gstInvoiceId=v;}
    public ProductStatus getStatus(){return status;}
    public void setStatus(ProductStatus v){status=v;}
    public byte[] getImageData(){return imageData;}
    public void setImageData(byte[] v){imageData=v;}
    public String getImageContentType(){return imageContentType;}
    public void setImageContentType(String v){imageContentType=v;}
    public String getImageFileName(){return imageFileName;}
    public void setImageFileName(String v){imageFileName=v;}

    public boolean isOutOfStock(){
        return currentStock == null || currentStock.signum() <= 0;
    }

    // Values displayed on the product screen and also reproducible from DB values.
    public BigDecimal getGstAmountPerUnit(){
        if(purchaseRate==null || gstPercentage==null) return BigDecimal.ZERO;
        return purchaseRate.multiply(gstPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
    public BigDecimal getPurchaseRateIncludingGst(){
        if(purchaseRate==null) return BigDecimal.ZERO;
        return purchaseRate.add(getGstAmountPerUnit()).setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getOpeningStockValueExclGst(){
        if(openingStock==null || purchaseRate==null) return BigDecimal.ZERO;
        return openingStock.multiply(purchaseRate).setScale(2, RoundingMode.HALF_UP);
    }
}
