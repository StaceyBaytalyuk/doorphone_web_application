package com.nuos.stakeyka.doorphone.domain;

import com.nuos.stakeyka.doorphone.util.Util;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "contract_date", columnDefinition = "DATE")
    private LocalDate contractDate;

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "balance")
    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @Column(name = "apartment")
    private Integer apartment;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    public Client() {}

    private Client(ClientBuilder builder) {
        name = builder.name;
        phone = builder.phone;
        address = builder.address;
        apartment = builder.apartment;
        contractDate = builder.contractDate;
        deviceType = builder.deviceType;
        status = builder.status;
        tariff = builder.tariff;
        balance = builder.balance;
        master = builder.master;
    }

    public void updateBalance(int d) {
        balance += d;
    }

    public static class ClientBuilder {
        private String name = "-", phone = "-", deviceType = "-";
        private Integer balance = 0, apartment = 0;
        private LocalDate contractDate = LocalDate.now();
        private Boolean status = false;
        private Address address = null;
        private Tariff tariff = null;
        private Master master = null;

        public ClientBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ClientBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public ClientBuilder setDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public ClientBuilder setTariff(Tariff tariff) {
            if ( tariff != null ) {
                this.tariff = tariff;
            }
            return this;
        }

        public ClientBuilder setBalance(Integer balance) {
            if ( !balance.toString().isEmpty() ) {
                this.balance = balance;
            }
            return this;
        }

        public ClientBuilder setApartment(Integer apartment) {
            if ( apartment != null ) {
                this.apartment = apartment;
            }
            return this;
        }

        public ClientBuilder setAddress(Address address) {
            this.address = address;
            return this;
        }

        public ClientBuilder setContractDate(String contractDate) {
            LocalDate date = Util.parseDate(contractDate);
            if ( date!=null ) {
                this.contractDate = date;
            }
            return this;
        }

        public ClientBuilder setContractDate(LocalDate contractDate) {
            if ( contractDate!=null ) {
                this.contractDate = contractDate;
            } else {
                this.contractDate = LocalDate.now();
            }
            return this;
        }

        public ClientBuilder setStatus(Boolean status) {
            if ( !status.toString().isEmpty() ) {
                this.status = status;
            }
            return this;
        }

        public ClientBuilder setMaster(Master master) {
            this.master = master;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }

    public void setContractDate(String contractDate) {
        if ( !contractDate.isEmpty() ) {
            this.contractDate = LocalDate.parse(contractDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
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

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Integer getApartment() {
        return apartment;
    }

    public void setApartment(Integer apartment) {
        this.apartment = apartment;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

}
