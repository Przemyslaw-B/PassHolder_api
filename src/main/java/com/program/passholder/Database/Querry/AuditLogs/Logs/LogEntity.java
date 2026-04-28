package com.program.passholder.Database.Querry.AuditLogs.Logs;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name="event_logs")
@Access(AccessType.FIELD)
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="id_event")
    private int idEvent;
    @Column(name="id_user")
    private long idUser;
    @Column(name="id_record")
    private long idRecord;
    @Column(name="ip")
    private String ip;
    @Column(name="timestamp")
    private Timestamp timestamp;
    @Column(name="details")
    private String details;
    @Column(name="setted_by")
    private long settedBy;


    public long getId() {return this.id;}

    public void setIdEvent(int idEvent){this.idEvent = idEvent;}
    public int getIdEvent(){return this.idEvent;}

    public void setUserId(long idUser){this.idUser = idUser;}
    public long getUserId(){return this.idUser;}

    public void setIdRecord(long idRecord){this.idRecord = idRecord;}
    public long getIdRecord(){return this.idRecord;}

    public void setIp(String ip){this.ip = ip;}
    public String getIp(){return this.ip;}

    public Timestamp getTimestamp(){return this.timestamp;}

    public void setDetails(String details){this.details=details;}
    public String getDetails(){return this.details;}

    public long getSettedBy(){return this.settedBy;}
    public void setSettedBy(long settedBy){this.settedBy = settedBy;}
}
