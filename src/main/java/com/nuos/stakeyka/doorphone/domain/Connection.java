package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Connections")
public class Connection {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @Column(name = "type")
    private Boolean type;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "date_create", columnDefinition = "DATE")
    private LocalDate dateCreate;

    @Column(name = "date_complete", columnDefinition = "DATE")
    private LocalDate dateComplete;

    public Connection(Client client, boolean type) {
        this.client = client;
        this.master = client.getMaster();
        this.type = type;
        this.status = "создано";
        this.dateCreate = LocalDate.now();
        this.dateComplete = LocalDate.now();
    }

    public Connection() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(LocalDate dateComplete) {
        this.dateComplete = dateComplete;
    }

}
