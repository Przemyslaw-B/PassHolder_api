package com.program.passholder.Database.Querry.UserRole;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_role")
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "id_role", nullable = false)
    private Integer idRole;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "setted_by")
    private Long settedBy;

    public Long getId() {
        return id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSettedBy() {
        return settedBy;
    }

    public void setSettedBy(Long settedBy) {
        this.settedBy = settedBy;
    }
}