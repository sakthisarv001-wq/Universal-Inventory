package com.universal.engineers.model;

import jakarta.persistence.*;

@Entity
@Table(name="app_users")
public class AppUser {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,unique=true,length=80)
    private String username;
    @Column(nullable=false)
    private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20)
    private Role role;

    public AppUser() {}
    public AppUser(String username,String password,Role role){this.username=username;this.password=password;this.role=role;}
    public Long getId(){return id;} public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
}
