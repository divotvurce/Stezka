package stezka.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stezka.data.entities.ArticleEntity;
import stezka.data.repositories.ArticleRepository;
import stezka.models.dto.ArticleDTO;
import stezka.models.dto.mappers.ArticleMapper;
import stezka.models.services.ArticleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/inspirace")
public class BlogController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("")
    public String renderBloghome(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "4") int size,
                                 Model model) {
        // Zavolání (fetch) zvýrazněného článku - poslední vytvořený
        Optional<ArticleEntity> featuredArticleOptional = articleService.getFeaturedArticle();

        // Velikost stránky s dalšími vybranými články a odstranění zvýrazněného článku (aby nebyl 2x)
        int effectiveSize = (page == 0) ? size + 1 : 6;
        Pageable pageable = PageRequest.of(page, effectiveSize, Sort.by("createdAt").descending());
        Page<ArticleEntity> articlePage = articleRepository.findAll(pageable);


        List<ArticleEntity> filteredArticles = articlePage.getContent().stream()
                .filter(article -> featuredArticleOptional.isEmpty() ||
                        !Objects.equals(article.getArticleId(), featuredArticleOptional.get().getArticleId()))
                .toList();

        List<ArticleEntity> articlesToDisplay;
        if (page == 0) {

            articlesToDisplay = filteredArticles.stream()
                    .limit(size)
                    .toList();
            featuredArticleOptional.ifPresent(article -> model.addAttribute("featuredArticle", article));
        } else {

            articlesToDisplay = filteredArticles;
        }

        // výpočet počtu článků a stránek
        long totalArticlesExcludingFeatured = articleRepository.count() - (featuredArticleOptional.isPresent() ? 1 : 0);
        int totalPages = (int) Math.ceil((double) (totalArticlesExcludingFeatured - (page == 0 ? size : 0)) / 6) + 1;

        if (page >= totalPages) {
            return "redirect:/inspirace?page=" + (totalPages - 1) + "&size=" + size;
        }

        model.addAttribute("articles", articlesToDisplay);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalArticles", totalArticlesExcludingFeatured);

        return "pages/blog/bloghome";
    }


    @GetMapping("/clanek/{articleId}")
    public String renderDetail(
            @PathVariable long articleId,
            Model model
    ) {
        ArticleDTO article = articleService.getById(articleId);
        model.addAttribute("article", article);

        return "pages/blog/blogpost";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/vytvorit")
    public String renderCreateForm(@ModelAttribute ArticleDTO article) {
        return "pages/blog/create";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/vytvorit")
    public String createArticle(
            @Valid @ModelAttribute ArticleDTO article,
            BindingResult result,
            @RequestParam("image") MultipartFile imageFile,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return renderCreateForm(article);
        }

        try {
            if (!imageFile.isEmpty()) {
                // Složka pro ukládání obrázků ke článkům
                String uploadDir = "src/main/resources/static/uploads/images/";

                // Generování unikátního jména pro obrázek
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // uložení obrázku
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());

                article.setImagePath("/uploads/images/" + fileName);
            }
            // Uložení článku s cestou k obrázku
            articleService.create(article);


            redirectAttributes.addFlashAttribute("success", "Článek byl úspěšně vytvořen.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Došlo k chybě při nahrávání obrázku.");
            e.printStackTrace();
            return renderCreateForm(article);
        }

        return "redirect:/inspirace";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/editovat/{articleId}")
    public String renderEditForm(@PathVariable Long articleId, @ModelAttribute ArticleDTO article) {
        ArticleDTO fetchedArticle = articleService.getById(articleId);
        articleMapper.updateArticleDTO(fetchedArticle, article);
        return "pages/blog/edit";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/editovat/{articleId}")
    public String editArticle(
            @PathVariable long articleId,
            @Valid @ModelAttribute ArticleDTO article,
            BindingResult result,
            @RequestParam("image") MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {

            ArticleDTO existingArticle = articleService.getById(articleId);
            article.setImagePath(existingArticle.getImagePath());
            model.addAttribute("article", article);
            return "pages/blog/edit";
        }

        try {

            if (!imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/images/";
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());


                article.setImagePath("/uploads/images/" + fileName);
            } else {

                ArticleDTO existingArticle = articleService.getById(articleId);
                article.setImagePath(existingArticle.getImagePath());
            }


            article.setArticleId(articleId);
            articleService.edit(article);

            redirectAttributes.addFlashAttribute("success", "Článek byl úspěšně upraven.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Došlo k chybě při nahrávání obrázku.");
            e.printStackTrace();


            model.addAttribute("article", article);
            return "pages/blog/edit";
        }

        return "redirect:/inspirace";
    }

    @DeleteMapping("/smazat/{articleId}")
    public String deleteArticle(
            @PathVariable long articleId,
            RedirectAttributes redirectAttributes
    ) {
        articleService.remove(articleId);
        redirectAttributes.addFlashAttribute("success", "Článek smazán.");
        return "redirect:/inspirace";
    }
}
