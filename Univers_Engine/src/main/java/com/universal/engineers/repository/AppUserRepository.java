package com.universal.engineers.repository;
import com.universal.engineers.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AppUserRepository extends JpaRepository<AppUser,Long>{ Optional<AppUser> findByUsername(String username); }
