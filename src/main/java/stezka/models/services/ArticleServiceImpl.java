package stezka.models.services;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stezka.data.entities.ArticleEntity;
import stezka.data.entities.UserEntity;
import stezka.data.repositories.ArticleRepository;
import stezka.data.repositories.UserRepository;
import stezka.models.dto.ArticleDTO;
import stezka.models.dto.mappers.ArticleMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void create(ArticleDTO article) {

        // Získání jména podle přihlášení (přihlašuje se e-mailem)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Získání Usera pomocí emailu
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Získání celého jména
        String authorName = userEntity.getFullName();

        // Nastavení jména autora článku a uložení
        ArticleEntity newArticle = articleMapper.toEntity(article);
        newArticle.setAuthorName(authorName);

        articleRepository.save(newArticle);
    }
    @Override
    public List<ArticleDTO> getAll() {
        return StreamSupport.stream(articleRepository.findAll().spliterator(), false)
                .map(i -> articleMapper.toDTO(i))
                .toList();
    }
    @Override
    public ArticleDTO getById(long articleId) {
        ArticleEntity fetchedArticle = articleRepository
                .findById(articleId)
                .orElseThrow();
        return articleMapper.toDTO(fetchedArticle);
    }
    @Override
    public void edit(ArticleDTO article) {
        ArticleEntity fetchedArticle = getArticleOrThrow(article.getArticleId());

        articleMapper.updateArticleEntity(article, fetchedArticle);
        articleRepository.save(fetchedArticle);
    }

    private ArticleEntity getArticleOrThrow(long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow();
    }

    public Optional<ArticleEntity> getFeaturedArticle() {
        return articleRepository.findFirstByOrderByCreatedAtDesc();
    }
    public List<ArticleDTO> getLatestArticles(int limit) {
        // Získání nejnovějších článků limitováno pomocí stránkování
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        Page<ArticleEntity> articlePage = articleRepository.findAll(pageable);

        // Převedení článků na DTO a zobrazení
        return articlePage.stream()
                .map(articleMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public void remove(long articleId) {
        ArticleEntity fetchedEntity = getArticleOrThrow(articleId);
        articleRepository.delete(fetchedEntity);
    }
}
