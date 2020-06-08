package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;

@Entity
@Table(name = "Masters")
public class Master {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "status")
    private Boolean status=true;

    public Master() {}

    public Master(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }

    public Master(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
