package stezka.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prirucka")
public class PriruckaController {

    @GetMapping("")
    public String renderUvod(Model model) {
        model.addAttribute("activePage", "uvod");
        return "pages/prirucka/uvod";
    }
    @GetMapping("/proc-ucit")
    public String renderProcUcit(Model model) {
        model.addAttribute("activePage", "proc-ucit");
        return "pages/prirucka/proc-ucit";
    }
    @GetMapping("/zacatky")
    public String renderZacatky(Model model) {
        model.addAttribute("activePage", "zacatky");
        return "pages/prirucka/zacatky";
    }
    @GetMapping("/absolvent")
    public String renderAbsolvent(Model model) {
        model.addAttribute("activePage", "absolvent");
        return "pages/prirucka/absolvent";
    }
    @GetMapping("/priprava")
    public String renderPriprava(Model model) {
        model.addAttribute("activePage", "priprava");
        return "pages/prirucka/priprava";
    }
    @GetMapping("/metody")
    public String renderMetody(Model model) {
        model.addAttribute("activePage", "metody");
        return "pages/prirucka/metody";
    }
    @GetMapping("/hodnoceni")
    public String renderHodnoceni(Model model) {
        model.addAttribute("activePage", "hodnoceni");
        return "pages/prirucka/hodnoceni";
    }
    @GetMapping("/byrokracie")
    public String renderByrokracie(Model model) {
        model.addAttribute("activePage", "byrokracie");
        return "pages/prirucka/byrokracie";
    }
    @GetMapping("/komunikace")
    public String renderKomunikace(Model model) {
        model.addAttribute("activePage", "komunikace");
        return "pages/prirucka/komunikace";
    }
    @GetMapping("/rozvoj")
    public String renderRozvoj(Model model) {
        model.addAttribute("activePage", "rozvoj");
        return "pages/prirucka/rozvoj";
    }
    @GetMapping("/psychika")
    public String renderPsychika(Model model) {
        model.addAttribute("activePage", "psychika");
        return "pages/prirucka/psychika";
    }
    @GetMapping("/problemy")
    public String renderProblemy(Model model) {
        model.addAttribute("activePage", "problemy");
        return "pages/prirucka/problemy";
    }
}
