package stezka.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stezka.data.entities.ArticleEntity;
import stezka.data.repositories.ArticleRepository;
import stezka.models.dto.ArticleDTO;
import stezka.models.dto.mappers.ArticleMapper;
import stezka.models.services.ArticleService;

import java.util.List;

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
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ArticleEntity> articlePage = articleRepository.findAll(pageable);

        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("totalArticles", articlePage.getTotalElements());

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
    @GetMapping("/vytvorit")
    public String renderCreateForm(@ModelAttribute ArticleDTO article) {
        return "pages/blog/create";
    }
    @PostMapping("/vytvorit")
    public String createArticle(
            @Valid @ModelAttribute ArticleDTO article,
            BindingResult result
    ) {
        if (result.hasErrors())
            return renderCreateForm(article);

        articleService.create(article);

        return "redirect:/inspirace";
    }
    @GetMapping("/editovat/{articleId}")
    public String renderEditForm(@PathVariable Long articleId, @ModelAttribute ArticleDTO article) {
        ArticleDTO fetchedArticle = articleService.getById(articleId);
        articleMapper.updateArticleDTO(fetchedArticle, article);
        return "pages/blog/edit";
    }
    @PostMapping("/editovat/{articleId}")
    public String editArticle(
            @PathVariable long articleId,
            @Valid ArticleDTO article,
            BindingResult result
    ) {
        if (result.hasErrors())
            return renderEditForm(articleId, article);

        article.setArticleId(articleId);
        articleService.edit(article);

        return "redirect:/inspirace";
    }
}

