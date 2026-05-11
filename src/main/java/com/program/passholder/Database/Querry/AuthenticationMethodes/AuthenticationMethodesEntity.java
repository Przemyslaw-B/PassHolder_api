package com.program.passholder.Database.Querry.AuthenticationMethodes;

import com.program.passholder.Security.EncryptionConverter;
import jakarta.persistence.*;


@Entity
@Table(name = "notification_methodes")
@Access(AccessType.FIELD)
public class AuthenticationMethodesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;

    public int getId() {return id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
