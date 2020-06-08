package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;

@Entity
@Table(name = "Streets")
public class Street {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    public Street() {}

    public Street(String name) {
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
}