package com.nuos.stakeyka.doorphone.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("mainMenu")
    public String mainMenu() {
        return "main_menu";
    }

    @GetMapping("menuClients")
    public String menuClients() {
        return "menu_clients";
    }

    @GetMapping("menuFinance")
    public String menuFinance() {
        return "menu_finance";
    }

    @GetMapping("menuMaterial")
    public String menuMaterial() {
        return "menu_material";
    }

    @GetMapping("menuRequests")
    public String menuRequests() {
        return "menu_requests";
    }

}