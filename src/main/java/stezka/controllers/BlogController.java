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
        // Fetch the featured article
        Optional<ArticleEntity> featuredArticleOptional = articleService.getFeaturedArticle();

        // Determine page size and exclude the featured article dynamically
        int effectiveSize = (page == 0) ? size + 1 : 6; // First page includes featured article
        Pageable pageable = PageRequest.of(page, effectiveSize, Sort.by("createdAt").descending());
        Page<ArticleEntity> articlePage = articleRepository.findAll(pageable);

        // Exclude the featured article from the list
        List<ArticleEntity> filteredArticles = articlePage.getContent().stream()
                .filter(article -> featuredArticleOptional.isEmpty() ||
                        !Objects.equals(article.getArticleId(), featuredArticleOptional.get().getArticleId()))
                .toList();

        // Prepare articles for display
        List<ArticleEntity> articlesToDisplay;
        if (page == 0) {
            // First page: Add featured article to model
            articlesToDisplay = filteredArticles.stream()
                    .limit(size)
                    .toList();
            featuredArticleOptional.ifPresent(article -> model.addAttribute("featuredArticle", article));
        } else {
            // Other pages: Display all articles
            articlesToDisplay = filteredArticles;
        }

        // Calculate total articles and pages
        long totalArticlesExcludingFeatured = articleRepository.count() - (featuredArticleOptional.isPresent() ? 1 : 0);
        int totalPages = (int) Math.ceil((double) (totalArticlesExcludingFeatured - (page == 0 ? size : 0)) / 6) + 1;

        // Handle out-of-range pages
        if (page >= totalPages) {
            return "redirect:/inspirace?page=" + (totalPages - 1) + "&size=" + size;
        }

        // Update model attributes
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
            @RequestParam("image") MultipartFile imageFile,  // Ensure you get the file here
            RedirectAttributes redirectAttributes  // For feedback messages
    ) {
        if (result.hasErrors()) {
            return renderCreateForm(article);
        }

        try {
            if (!imageFile.isEmpty()) {
                // Define upload directory (relative to the project directory or server path)
                String uploadDir = "src/main/resources/static/uploads/images/";

                // Generate a unique file name
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                // Ensure the directory exists
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save the file
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());

                // Set the file path in the DTO (relative path)
                article.setImagePath("/uploads/images/" + fileName);
            }
            // Save the article (with image path)
            articleService.create(article);

            // Redirect with a success message
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
            @RequestParam("image") MultipartFile imageFile, // Image file input
            Model model, // To pass data back to the form
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            // Include the existing article data in the model to redisplay the form
            ArticleDTO existingArticle = articleService.getById(articleId);
            article.setImagePath(existingArticle.getImagePath());
            model.addAttribute("article", article);
            return "pages/blog/edit";
        }

        try {
            // Check if a new image is uploaded
            if (!imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/images/";
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageFile.getBytes());

                // Set the new image path
                article.setImagePath("/uploads/images/" + fileName);
            } else {
                // Retain the existing image path
                ArticleDTO existingArticle = articleService.getById(articleId);
                article.setImagePath(existingArticle.getImagePath());
            }

            // Update the article
            article.setArticleId(articleId);
            articleService.edit(article);

            redirectAttributes.addFlashAttribute("success", "Článek byl úspěšně upraven.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Došlo k chybě při nahrávání obrázku.");
            e.printStackTrace();

            // Re-display the form with the entered data
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
