package com.program.passholder.Database.Querry.Roles;

import jakarta.persistence.*;

@Entity
@Table(name="roles")
@Access(AccessType.FIELD)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name")
    private String name;

    public int getId() {return id;}

    public String getName() {return name;}
    public void setName(String name){this.name=name;}

}
