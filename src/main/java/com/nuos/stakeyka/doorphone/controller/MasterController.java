package com.nuos.stakeyka.doorphone.controller;

import com.nuos.stakeyka.doorphone.domain.Master;
import com.nuos.stakeyka.doorphone.repos.ClientRepo;
import com.nuos.stakeyka.doorphone.repos.MasterRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class MasterController {
    private final MasterRepo masterRepo;
    private final ClientRepo clientRepo;

    public MasterController(MasterRepo masterRepo, ClientRepo clientRepo) {
        this.masterRepo = masterRepo;
        this.clientRepo = clientRepo;
    }

    //////////////////////////////////////////////////////////////////////////
    // MASTER
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("masters")
    public String masters(Map<String, Object> model) {
        Iterable<Master> masters = masterRepo.findAll();
        model.put("masters", masters);
        return "masters";
    }

    @PostMapping("addMaster")
    public String addMaster(@RequestParam String name, @RequestParam String phone, Map<String, Object> model) {
        if ( !name.isEmpty() && !phone.isEmpty() ) {
            if ( phone.length()>10 ) {
                phone = phone.substring(0, 10);
            }
            Master master = new Master(name, phone);
            masterRepo.save(master);
        } else {
            model.put("message", "Невозможно зарегистрировать мастера. Введите все поля.");
        }
        return masters(model);
    }

    @PostMapping("editMaster")
    public String editMaster(@RequestParam Integer id, @RequestParam String name, @RequestParam String phone, Map<String, Object> model) {
        if ( id!=null && masterRepo.existsById(id) ) {
            Master master = masterRepo.findById(id).orElse(new Master());
            if ( !name.isEmpty() ) {
                master.setName(name);
            }
            if ( !phone.isEmpty() ) {
                if ( phone.length()>10 ) {
                    phone = phone.substring(0, 10);
                }
                master.setPhone(phone);
            }
        }
        return masters(model);
    }

    @PostMapping("deleteMaster")
    public String deleteMaster(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && masterRepo.existsById(id) ) {
            Master master = masterRepo.findById(id).orElse(new Master());
            if ( !clientRepo.existsByStatusAndMaster(true, master) ) { //нет подкл клиентов с этим мастером
                master.setStatus(false); // уволен
                model.put("message", master.getName()+" больше не работает");
            } else {
                model.put("message", "Нельзя удалить мастера, есть обслуживаемые клиенты");
            }
        } else {
            model.put("message", "Нет такого мастера");
        }
        return masters(model);
    }

}