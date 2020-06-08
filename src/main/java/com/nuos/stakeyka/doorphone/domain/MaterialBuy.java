package com.nuos.stakeyka.doorphone.domain;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MaterialBuy")
public class MaterialBuy {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "amount")
    private Integer amount = 0;

    @Column(name = "price")
    private Integer price = 0;

    public MaterialBuy(LocalDate date, Master master, Material material, Integer amount, Integer price) {
        this.date = date;
        this.master = master;
        this.material = material;
        this.amount = amount;
        this.price = price;
    }

    public MaterialBuy() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
