package stezka.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import stezka.data.entities.ArticleEntity;

public interface ArticleRepository extends CrudRepository<ArticleEntity, Long> {
    Page<ArticleEntity> findAll(Pageable pageable);
}
