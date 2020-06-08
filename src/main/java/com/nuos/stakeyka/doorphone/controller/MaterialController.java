package com.nuos.stakeyka.doorphone.controller;

import com.nuos.stakeyka.doorphone.domain.*;
import com.nuos.stakeyka.doorphone.repos.*;
import com.nuos.stakeyka.doorphone.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class MaterialController {
    private final MasterRepo masterRepo;
    private final ServiceRepo serviceRepo;
    private final MaterialRepo materialRepo;
    private final MaterialUseRepo useRepo;
    private final MaterialBuyRepo buyRepo;

    public MaterialController(MasterRepo masterRepo, ServiceRepo serviceRepo, MaterialRepo materialRepo, MaterialUseRepo useRepo, MaterialBuyRepo buyRepo) {
        this.masterRepo = masterRepo;
        this.serviceRepo = serviceRepo;
        this.materialRepo = materialRepo;
        this.useRepo = useRepo;
        this.buyRepo = buyRepo;
    }

    //////////////////////////////////////////////////////////////////////////
    // MATERIAL
    //////////////////////////////////////////////////////////////////////////

    @GetMapping("materials")
    public String materials(Map<String, Object> model) {
        Iterable<Material> materials = materialRepo.findAll();
        model.put("materials", materials);
        return "materials";
    }

    @PostMapping("addMaterial")
    public String addMaterial(@RequestParam String name, Map<String, Object> model) {
        if ( !name.isEmpty() ) {
            if ( !materialRepo.existsByName(name) ) { // не создавать материал с повтором имени
                Material material = new Material(name);
                materialRepo.save(material);
            } else {
                model.put("message", "Такой материал уже существует");
            }
        } else {
            model.put("message", "Введите название материала");
        }
        return materials(model);
    }

    @PostMapping("editMaterial")
    public String editMaterial(@RequestParam Integer id, @RequestParam String name,
                               @RequestParam Integer amount, Map<String, Object> model) {
        if ( id!=null && materialRepo.existsById(id) ) {
            Material material = materialRepo.findById(id).orElse(new Material());
            if ( amount!=null && (amount>=0) ) {
                material.setAmount(amount);
            }
            if ( !name.isEmpty() ) {
                material.setName(name);
            }
            materialRepo.save(material);
        } else {
            model.put("message", "Нет такого материала");
        }
        return materials(model);
    }


    //////////////////////////////////////////////////////////////////////////
    // BUY
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("materialBuy")
    public String materialBuy(Map<String, Object> model) {
        Iterable<MaterialBuy> buy = buyRepo.findAll();
        model.put("buy", buy);
        return "material_buy";
    }

    @PostMapping("addBuy")
    public String addBuy(@RequestParam String dateString, @RequestParam Integer materialID,
                         @RequestParam Integer masterID, @RequestParam Integer amount,
                         @RequestParam Integer price, Map<String, Object> model) {
        if ( amount!=null && amount>0 && price!=null && price>0 && materialID!=null && materialRepo.existsById(materialID) && masterID!=null && masterRepo.existsById(masterID) ) {
            LocalDate date = Util.parseDateOrNow(dateString);
            Master master = masterRepo.findById(masterID).orElse(new Master());
            Material material = materialRepo.findById(materialID).orElse(new Material());
            MaterialBuy buy = new MaterialBuy(date, master, material, amount, price);
            buyRepo.save(buy);
            material.updateAmount(amount);
            materialRepo.save(material);
        } else {
            model.put("message", "Данные введены неверно");
        }
        return materialBuy(model);
    }

    @PostMapping("deleteBuy")
    public String deleteBuy(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && buyRepo.existsById(id) ) {
            MaterialBuy buy = buyRepo.findById(id).orElse(new MaterialBuy());
            Material material = buy.getMaterial();
            if ( ( material.getAmount() - buy.getAmount() ) >= 0 ) { // чтобы не забрать больше чем есть
                material.updateAmount(-buy.getAmount()); // удаление закупки - уменьшить кол-во материала на складе
                materialRepo.save(material);
                buyRepo.deleteById(id);
            } model.put("message", "Нельзя удалить закупку");
        } model.put("message", "Нет такой закупки");
        return materialBuy(model);
    }


    //////////////////////////////////////////////////////////////////////////
    // USE
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("materialUse")
    public String materialUse(Map<String, Object> model) {
        Iterable<MaterialUse> use = useRepo.findAll();
        model.put("use", use);
        return "material_use";
    }

    @PostMapping("addUse")
    public String addUse(@RequestParam Integer serviceID, @RequestParam Integer materialID, @RequestParam Integer amount, Map<String, Object> model) {
        if ( amount!=null && amount>0 && serviceID!=null && serviceRepo.existsById(serviceID) && materialID!=null && materialRepo.existsById(materialID) ) {
            Material material = materialRepo.findById(materialID).orElse(new Material());
            Service service = serviceRepo.findById(serviceID).orElse(new Service());
            if ( material.getAmount() >= amount ) { // нельзя потратить больше чем есть на складе
                MaterialUse use = new MaterialUse(service, material, amount);
                useRepo.save(use);
                material.updateAmount(-amount);
                materialRepo.save(material);
            } else {
                model.put("message", "Недостаточно материала "+material.getName()+" на складе");
            }
        } else {
            model.put("message", "Данные введены неверно");
        }
        return materialUse(model);
    }

    @PostMapping("editUse")
    public String editUse(@RequestParam Integer id, @RequestParam Integer amount, Map<String, Object> model) {
        if ( amount!=null && amount>0 && id!=null && useRepo.existsById(id) ) {
            MaterialUse use = useRepo.findById(id).orElse(new MaterialUse());
            Material material = use.getMaterial();
            if ( !use.getAmount().equals(amount) ) { // не менять на то же самое
                if ( (material.getAmount() + use.getAmount()) >= amount ) {
                    material.updateAmount(use.getAmount() - amount); // вернули старое кол-во и забрали новое
                    materialRepo.save(material);
                    use.setAmount(amount);
                    useRepo.save(use);
                } else {
                    model.put("message", "Недостаточно материала "+material.getName()+" на складе");
                }
            }
        } else {
            model.put("message", "Данные введены неверно");
        }
        return materialUse(model);
    }

    @PostMapping("deleteUse")
    public String deleteUse(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && useRepo.existsById(id) ) {
            MaterialUse use = useRepo.findById(id).orElse(new MaterialUse());
            Material material = use.getMaterial();
            material.updateAmount(use.getAmount()); // вернули обратно на склад
            materialRepo.save(material);
            useRepo.deleteById(id);
        } else {
            model.put("message", "Неверный id");
        }
        return materialUse(model);
    }

}