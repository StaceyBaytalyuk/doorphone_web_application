package com.nuos.stakeyka.doorphone.domain;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Fees")
public class Fee {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "status")
    private Boolean status = false;

    public Fee() {}

    public Fee(Client client, LocalDate date) {
        this.client = client;
        this.tariff = client.getTariff();
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Client getClient() {
        return client;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }
}
