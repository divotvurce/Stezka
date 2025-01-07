package stezka.models.services;

import stezka.data.entities.ArticleEntity;
import stezka.models.dto.ArticleDTO;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    void create(ArticleDTO article);

    List<ArticleDTO> getAll();

    ArticleDTO getById(long articleId);

    void edit(ArticleDTO article);

    Optional<ArticleEntity> getFeaturedArticle();

    List<ArticleDTO> getLatestArticles(int limit);

    void remove(long articleId);

}
