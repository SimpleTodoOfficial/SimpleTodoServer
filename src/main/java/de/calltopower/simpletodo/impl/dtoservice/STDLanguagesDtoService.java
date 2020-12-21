package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDLanguagesDto;
import de.calltopower.simpletodo.impl.model.STDLanguagesModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the languages DTO
 */
@NoArgsConstructor
@Service
public class STDLanguagesDtoService implements STDDtoService<STDLanguagesDto, STDLanguagesModel> {

    @Override
    public STDLanguagesDto convert(STDLanguagesModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDLanguagesDto.builder()
                .languages(model.getLanguages())
                .build();
        // @formatter:on
    }

    @Override
    public STDLanguagesDto convertAbridged(STDLanguagesModel model) {
        return convert(model);
    }

    @Override
    public Set<STDLanguagesDto> convert(Set<STDLanguagesModel> models) {
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
    public Set<STDLanguagesDto> convertAbridged(Set<STDLanguagesModel> models) {
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
