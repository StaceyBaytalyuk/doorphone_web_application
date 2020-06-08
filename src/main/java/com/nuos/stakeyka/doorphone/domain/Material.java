package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;

@Entity
@Table(name = "Materials")
public class Material {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name = "-";

    @Column(name = "amount")
    private Integer amount = 0;

    public Material() {}

    public Material(String name) {
        this.name = name;
    }

    public void updateAmount(Integer d) {
        amount += d;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
