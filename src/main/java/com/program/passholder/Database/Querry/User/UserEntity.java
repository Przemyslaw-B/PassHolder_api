package com.program.passholder.Database.Querry.User;

import com.program.passholder.Security.EncryptionConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="email")
    @Convert(converter = EncryptionConverter.class)
    private String email;
    @Column(name="password")
    @Convert(converter = EncryptionConverter.class)
    private String password;
    @Column(name="phone")
    @Convert(converter = EncryptionConverter.class)
    private String phone;
    @Column(name="public_key")
    @Convert(converter = EncryptionConverter.class)
    private String publicKey;
    @Column(name="private_key")
    @Convert(converter = EncryptionConverter.class)
    private String privateKey;
    @Column(name="name")
    @Convert(converter = EncryptionConverter.class)
    private String name;
    @Column(name="token")
    @Convert(converter = EncryptionConverter.class)
    private String token;
    @Column(name="auth_key")
    @Convert(converter = EncryptionConverter.class)
    private String authKey;
    @Column(name="is_authorized")
    private int isAuthorized;
    @Column(name="email_activator")
    private String emailActivator;
    @Column(name="is_activated")
    private int isActivated;
    @Column(name="default_rotation")
    private int rotation;
    @Column(name="security_password")
    private String security_password;

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

    public String getAuthKey() {return authKey;}
    public void setAuthKey(String authKey) {this.authKey = authKey;}

    public int getIsAuthorized() {return isAuthorized;}
    public void setIsAuthorized(int isAuthorized) {this.isAuthorized = isAuthorized;}

    public String getEmailActivator() {return emailActivator;}
    public void setEmailActivator(String emailActivator) {this.emailActivator = emailActivator;}

    public int getIsActivated() {return isActivated;}
    public void setIsActivated(int isActivated) {this.isActivated = isActivated;}

    public int getRotation() {return rotation;}
    public void setRotation(int rotation) {this.rotation = rotation;}

    public String getSecurity_password() {return security_password;}
    public void setSecurity_password(String security_password) {this.security_password = security_password;}

}
