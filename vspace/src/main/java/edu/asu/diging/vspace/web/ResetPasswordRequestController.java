package edu.asu.diging.vspace.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResetPasswordRequestController {

    @RequestMapping(value="/reset/password/request")
    public String show(Model model) {
        return "resetPassword";
    }
}
