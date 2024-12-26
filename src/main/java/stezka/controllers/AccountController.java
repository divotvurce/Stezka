package stezka.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ucet")
public class AccountController {

    @GetMapping("/prihlaseni")
    public String renderLogin() {
        return "/pages/account/login";
    }
    @GetMapping("/registrace")
    public String renderRegister() {
        return "/pages/account/register";
    }
    @GetMapping("/zapomenuteheslo")
    public String renderPassword() {
        return "/pages/account/password";
    }
}
