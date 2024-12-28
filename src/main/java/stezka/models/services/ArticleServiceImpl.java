package stezka.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stezka.data.entities.ArticleEntity;
import stezka.data.repositories.ArticleRepository;
import stezka.models.dto.ArticleDTO;
import stezka.models.dto.mappers.ArticleMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void create(ArticleDTO article) {

        ArticleEntity newArticle = articleMapper.toEntity(article);

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
}