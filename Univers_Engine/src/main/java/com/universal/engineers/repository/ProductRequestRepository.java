package com.universal.engineers.repository;
import com.universal.engineers.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductRequestRepository extends JpaRepository<ProductRequest,Long>{ List<ProductRequest> findByUserOrderByRequestedAtDesc(AppUser user); }
