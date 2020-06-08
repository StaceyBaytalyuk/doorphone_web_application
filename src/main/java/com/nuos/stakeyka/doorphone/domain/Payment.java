package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Payments")
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "sum")
    private Integer sum = 0;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client = null;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date = null;

    @Column(name = "method")
    private Boolean method = false;

    public Payment() {}

    public Payment(Client client, Integer sum, Boolean method, LocalDate date) {
        this.sum = sum;
        this.client = client;
        this.date = date;
        this.method = method;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getMethod() {
        return method;
    }

    public void setMethod(Boolean method) {
        this.method = method;
    }
}
