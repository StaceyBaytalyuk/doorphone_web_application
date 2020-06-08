package com.nuos.stakeyka.doorphone.domain;
import javax.persistence.*;

@Entity
@Table(name = "MaterialUse")
public class MaterialUse {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "amount")
    private Integer amount = 0;

    public MaterialUse(Service service, Material material, Integer amount) {
        this.service = service;
        this.material = material;
        this.amount = amount;
    }

    public MaterialUse() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
