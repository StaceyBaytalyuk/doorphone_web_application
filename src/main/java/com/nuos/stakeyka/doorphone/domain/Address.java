package com.nuos.stakeyka.doorphone.domain;

import javax.persistence.*;

@Entity
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "street_id")
    private Street street=null;

    @Column(name = "house")
    private Integer house=0;

    @Column(name = "entrance")
    private Integer entrance=0;

    public Address() {}

    public Address(Street street, Integer house, Integer entrance) {
        this.street = street;
        this.house = house;
        this.entrance = entrance;
    }

    //    private Address(AddressBuilder builder) {
//        street = builder.street;
//        house = builder.house;
//        entrance = builder.entrance;
//    }

    @Override
    public String toString() {
        return  "id=" + id +
                " " + street.getName() +
                " " + house +
                " " + entrance;
    }
//
//    public static class AddressBuilder {
//        private Integer entrance=0, house=0;
//        private Street street = null;
//
//        public AddressBuilder setHouse(Integer house) {
//            if ( house != null ) {
//                this.house = house;
//            }
//            return this;
//        }
//
//        public AddressBuilder setEntrance(Integer entrance) {
//            if ( entrance != null ) {
//                this.entrance = entrance;
//            }
//            return this;
//        }
//
//        public AddressBuilder setStreet(Street street) {
//            this.street = street;
//            return this;
//        }
//
//        public Address build() {
//            return new Address(this);
//        }
//    }

    public Integer getId() {
        return id;
    }

    public Street getStreet() {
        return street;
    }

    public Integer getHouse() {
        return house;
    }

    public Integer getEntrance() {
        return entrance;
    }
}