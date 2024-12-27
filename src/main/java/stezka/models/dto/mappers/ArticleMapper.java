package stezka.models.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import stezka.data.entities.ArticleEntity;
import stezka.models.dto.ArticleDTO;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "imagePath", source = "imagePath")
    ArticleEntity toEntity(ArticleDTO source);

    @Mapping(target = "imagePath", source = "imagePath")
    ArticleDTO toDTO(ArticleEntity source);

    @Mapping(target = "imagePath", source = "imagePath")
    void updateArticleDTO(ArticleDTO source, @MappingTarget ArticleDTO target);

    @Mapping(target = "imagePath", source = "imagePath")
    void updateArticleEntity(ArticleDTO source, @MappingTarget ArticleEntity target);

}
