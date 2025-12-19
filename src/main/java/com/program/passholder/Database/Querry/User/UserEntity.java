package com.program.passholder.Database.Querry.User;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="phone")
    private String phone;
    @Column(name="public_key")
    private String publicKey;
    @Column(name="private_key")
    private String privateKey;
    @Column(name="name")
    private String name;
    @Column(name="token")
    private String token;


    public Long getId() {return id;}
    //public void setId(Long id) {this.id = id;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public String getPublicKey() {return publicKey;}
    public void setPublicKey(String publicKey) {this.publicKey = publicKey;}

    public String getPrivateKey() {return privateKey;}
    public void setPrivateKey(String privateKey) {this.privateKey = privateKey;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

}
