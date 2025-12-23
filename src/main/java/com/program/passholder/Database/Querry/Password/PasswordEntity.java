package com.program.passholder.Database.Querry.Password;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="passwords")
@Access(AccessType.FIELD)
public class PasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private Long userId;
    @Column(name="url")
    private String url;
    @Column(name="login")
    private String login;
    @Column(name="password")
    private String password;
    @Column(name="exp_date")
    private Date expDate;
    @Column(name="modify_date")
    private Date modifyDate;

    public Long getId() {return id;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}

    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Date getExpDate() {return expDate;}
    public void setExpDate(Date exp_date) {this.expDate = exp_date;}

    public Date getModifyDate() {return modifyDate;}
    public void setModifyDate(Date modify_date) {this.modifyDate = modify_date;}

}
