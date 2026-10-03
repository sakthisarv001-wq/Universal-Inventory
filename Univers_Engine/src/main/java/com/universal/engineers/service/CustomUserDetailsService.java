package com.universal.engineers.service;
import com.universal.engineers.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService{
 private final AppUserRepository repo; public CustomUserDetailsService(AppUserRepository repo){this.repo=repo;}
 @Override public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{return repo.findByUsername(username).map(u->User.withUsername(u.getUsername()).password(u.getPassword()).authorities(List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name()))).build()).orElseThrow(()->new UsernameNotFoundException("Invalid username or password"));}
}
