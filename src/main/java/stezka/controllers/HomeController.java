package stezka.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import stezka.data.entities.ArticleEntity;
import stezka.data.entities.NewsletterEntity;
import stezka.data.repositories.NewsletterRepository;
import stezka.models.dto.ArticleDTO;
import stezka.models.dto.NewsletterDTO;
import stezka.models.services.ArticleService;
import stezka.models.services.ArticleServiceImpl;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NewsletterRepository repository;

    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @GetMapping("/")
    public String showNewsletterForm(Model model) {

        model.addAttribute("newsletterDTO", new NewsletterDTO());

        List<ArticleDTO> latestArticles = articleServiceImpl.getLatestArticles(3);
        model.addAttribute("newsletterDTO", new NewsletterDTO());
        model.addAttribute("latestArticles", latestArticles);
        return "pages/home/index";
    }

    @PostMapping("/")
    public String saveNewsletterForm(
            @Valid @ModelAttribute("newsletterDTO") NewsletterDTO newsletterDTO,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Formulář obsahuje chyby. Zkontrolujte prosím svůj e-mail.");
            model.addAttribute("newsletterDTO", newsletterDTO);
            return "pages/home/index";
        }

        // uložení do databáze
        NewsletterEntity subscriber = new NewsletterEntity();
        subscriber.setEmail(newsletterDTO.getEmail());
        repository.save(subscriber);

        model.addAttribute("newsletterDTO", new NewsletterDTO());


        model.addAttribute("success", "Odesláno. Děkujeme za Váš zájem!");
        return "pages/home/index";
    }
    @GetMapping("/onas")
    public String renderAbout() {
        return "pages/home/about";
    }
}
