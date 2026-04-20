package com.program.passholder.Database.Querry.AuditLogs.Events;

import jakarta.persistence.*;

@Entity
@Table(name="events")
@Access(AccessType.FIELD)
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
