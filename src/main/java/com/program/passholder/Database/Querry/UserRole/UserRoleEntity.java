package com.program.passholder.Database.Querry.UserRole;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_role")
@Access(AccessType.FIELD)
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="id_user")
    private long idUser;
    @Column(name="id_role")
    private int idRole;
    @Column(name="timestamp")
    private Timestamp timestamp;
    @Column(name="setted_by")
    private long settedBy;

    public long getId(){return id;}

    public long getIdUser(){return idUser;}
    public void setIdUser(long idUser){this.idUser=idUser;}

    public int getIdRole(){return idRole;}
    public void setIdRole(int idRole){this.idRole=idRole;}

    public Timestamp getTimestamp(){return timestamp;}

    public long getSettedBy(){return settedBy;}
    public void setSettedBy(long settedBy){this.settedBy=settedBy;}
}
