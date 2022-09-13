package com.peoples.admin.peoples_admin.controller;

import com.peoples.admin.peoples_admin.domain.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal SecurityUser admin) {
        if (admin != null) {
            return "redirect:/dashboard";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
}
