package com.peoples.admin.peoples_admin.controller;

import com.peoples.admin.peoples_admin.domain.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private static final String CURRENT_MENU = "current";

    @GetMapping("/dashboard")
    public String dataSource(Model model) {
        model.addAttribute(CURRENT_MENU, "/dashboard");
        return "view/dashboard";
    }

    @GetMapping("/usermgmt")
    public String usermgmt(Model model) {
        model.addAttribute(CURRENT_MENU, "/usermgmt");
        return "view/usermgmt";
    }

    @GetMapping("/adminmgmt")
    public String adminmgmt(Model model) {
        model.addAttribute(CURRENT_MENU, "/adminmgmt");
        return "view/adminmgmt";
    }

    @GetMapping("/studymgmt")
    public String studymgmt(Model model) {
        model.addAttribute(CURRENT_MENU, "/studymgmt");
        return "view/studymgmt";
    }

    @GetMapping("/log")
    public String log(Model model) {
        model.addAttribute(CURRENT_MENU, "/log");
        return "view/log";
    }
}
