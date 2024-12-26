package stezka.models.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import stezka.data.entities.ArticleEntity;
import stezka.models.dto.ArticleDTO;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleEntity toEntity(ArticleDTO source);

    ArticleDTO toDTO(ArticleEntity source);

    void updateArticleDTO(ArticleDTO source, @MappingTarget ArticleDTO target);

    void updateArticleEntity(ArticleDTO source, @MappingTarget ArticleEntity target);

}
