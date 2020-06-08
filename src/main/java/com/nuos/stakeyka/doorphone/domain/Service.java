package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Services")
public class Service {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @Column(name = "comment_client", length = 300)
    private String commentClient;

    @Column(name = "comment_master", length = 300)
    private String commentMaster;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "date_create", columnDefinition = "DATE")
    private LocalDate dateCreate;

    @Column(name = "date_complete", columnDefinition = "DATE")
    private LocalDate dateComplete;

    public Service(Client client, String commentClient) {
        this.client = client;
        this.master = client.getMaster();
        this.commentMaster = "-";
        this.status = "создано";
        this.dateCreate = LocalDate.now();
        this.dateComplete = LocalDate.now();
        this.commentClient = commentClient;
    }

    public Service() {}

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

    public String getCommentClient() {
        return commentClient;
    }

    public void setCommentClient(String commentClient) {
        if ( commentClient.isEmpty() ) {
            this.commentClient = "-";
        } else {
            this.commentClient = commentClient;
        }
    }

    public String getCommentMaster() {
        return commentMaster;
    }

    public void setCommentMaster(String commentMaster) {
        if ( commentMaster.isEmpty() ) {
            this.commentMaster = "-";
        } else {
            this.commentMaster = commentMaster;
        }
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
