package stezka.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/onas")
    public String renderAbout() {
        return "pages/home/about";
    }
    @GetMapping("/prirucka")
    public String renderPrirucka() {
        return "pages/home/prirucka";
    }
}
