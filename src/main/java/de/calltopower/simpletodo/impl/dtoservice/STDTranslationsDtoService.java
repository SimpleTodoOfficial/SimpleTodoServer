package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDTranslationsDto;
import de.calltopower.simpletodo.impl.model.STDTranslationsModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the translations DTO
 */
@NoArgsConstructor
@Service
public class STDTranslationsDtoService implements STDDtoService<STDTranslationsDto, STDTranslationsModel> {

    @Override
    public STDTranslationsDto convert(STDTranslationsModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDTranslationsDto.builder()
                .translations(model.getTranslations())
                .build();
        // @formatter:on
    }

    @Override
    public STDTranslationsDto convertAbridged(STDTranslationsModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDTranslationsDto.builder()
                .translations(model.getTranslations())
                .build();
        // @formatter:on
    }

    @Override
    public Set<STDTranslationsDto> convert(Set<STDTranslationsModel> models) {
        if (models == null) {
            return new HashSet<>();
        }

        // @formatter:off
        return models.stream()
                        .map(m -> convert(m))
                        .collect(Collectors.toSet());
        // @formatter:on
    }

    @Override
    public Set<STDTranslationsDto> convertAbridged(Set<STDTranslationsModel> models) {
        if (models == null) {
            return new HashSet<>();
        }

        // @formatter:off
        return models.stream()
                        .map(m -> convertAbridged(m))
                        .collect(Collectors.toSet());
        // @formatter:on
    }

}
