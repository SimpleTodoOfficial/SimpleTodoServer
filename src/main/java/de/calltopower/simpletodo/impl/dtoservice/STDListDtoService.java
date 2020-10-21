package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDListDto;
import de.calltopower.simpletodo.impl.model.STDListModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the list DTO
 */
@NoArgsConstructor
@Service
public class STDListDtoService implements STDDtoService<STDListDto, STDListModel> {

    private STDTodoDtoService todoDtoService;

    @Autowired
    public STDListDtoService(STDTodoDtoService todoDtoService) {
        this.todoDtoService = todoDtoService;
    }

    @Override
    public STDListDto convert(STDListModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDListDto.builder()
                .id(model.getId())
                //.createdDate(model.getCreatedDate())
                .name(model.getName())
                .workspaceId(model.getWorkspace().getId())
                .workspaceName(model.getWorkspace().getName())
                .todos(todoDtoService.convertAbridged(model.getTodos()))
                .jsonData(model.getJsonData())
                .build();
        // @formatter:on
    }

    @Override
    public STDListDto convertAbridged(STDListModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDListDto.builder()
                .id(model.getId())
                .name(model.getName())
                .workspaceId(model.getWorkspace().getId())
                .build();
        // @formatter:on
    }

    @Override
    public Set<STDListDto> convert(Set<STDListModel> models) {
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
    public Set<STDListDto> convertAbridged(Set<STDListModel> models) {
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
