package com.universal.engineers.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="product_requests")
public class ProductRequest {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.EAGER) private AppUser user;
    @ManyToOne(optional=false,fetch=FetchType.EAGER) private Product product;
    @Column(nullable=false,length=30) private String requestedUom;
    @Column(nullable=false,precision=18,scale=3) private BigDecimal requiredQuantity;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private RequestStatus status=RequestStatus.PENDING;
    @Column(nullable=false) private LocalDateTime requestedAt=LocalDateTime.now();
    public Long getId(){return id;} public AppUser getUser(){return user;} public void setUser(AppUser v){user=v;}
    public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public String getRequestedUom(){return requestedUom;} public void setRequestedUom(String v){requestedUom=v;}
    public BigDecimal getRequiredQuantity(){return requiredQuantity;} public void setRequiredQuantity(BigDecimal v){requiredQuantity=v;}
    public RequestStatus getStatus(){return status;} public void setStatus(RequestStatus v){status=v;}
    public LocalDateTime getRequestedAt(){return requestedAt;}
}
