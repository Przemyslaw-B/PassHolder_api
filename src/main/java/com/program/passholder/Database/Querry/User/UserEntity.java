package com.program.passholder.Database.Querry.User;

import com.program.passholder.Security.EncryptionConverter;
import jakarta.persistence.*;

import java.util.Date;

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
    @Convert(converter = EncryptionConverter.class)
    private String password;
    @Column(name="phone")
    //@Convert(converter = EncryptionConverter.class)
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
    @Column(name="security_password")
    @Convert(converter = EncryptionConverter.class)
    private String security_password;
    @Column(name = "notification_method")
    private int notificationMethod;
    @Column(name="totp_secret")
    private String totpSecret;
    @Column(name="password_reset_token")
    @Convert(converter = EncryptionConverter.class)
    private String passwordResetToken;
    @Column(name="locked_until")
    private Date lockedUntil;
    @Column(name="failed_attempts")
    private Integer failedAttempts;


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

    public String getSecurity_password() {return security_password;}
    public void setSecurity_password(String security_password) {this.security_password = security_password;}

    public int getNotificationMethod() {return notificationMethod;}
    public void setNotificationMethod(int notificationMethod) {this.notificationMethod = notificationMethod;}

    public String getTotpSecret() {return totpSecret;}
    public void setTotpSecret(String totpSecret) {this.totpSecret = totpSecret;}

    public String getPasswordResetToken() {return passwordResetToken;}

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public Date getLockedUntil() {return lockedUntil;}
    public void setLockedUntil(Date lockTermin){
        this.lockedUntil = lockTermin;
    };

    public Integer getFailedAttempts() {return failedAttempts;}
    public void setFailedAttempts(Integer failedAttempts) {this.failedAttempts = failedAttempts;}
}
