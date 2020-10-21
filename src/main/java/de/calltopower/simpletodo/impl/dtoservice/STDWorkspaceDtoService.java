package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDWorkspaceDto;
import de.calltopower.simpletodo.impl.model.STDWorkspaceModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the workspace DTO
 */
@NoArgsConstructor
@Service
public class STDWorkspaceDtoService implements STDDtoService<STDWorkspaceDto, STDWorkspaceModel> {

    private STDListDtoService listDtoService;
    private STDUserDtoService userDtoService;

    @Autowired
    public STDWorkspaceDtoService(STDListDtoService listDtoService, STDUserDtoService userDtoService) {
        this.listDtoService = listDtoService;
        this.userDtoService = userDtoService;
    }

    @Override
    public STDWorkspaceDto convert(STDWorkspaceModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDWorkspaceDto.builder()
                .id(model.getId())
                //.createdDate(model.getCreatedDate())
                .name(model.getName())
                .users(userDtoService.convertAbridged(model.getUsers()))
                .lists(listDtoService.convertAbridged(model.getLists()))
                .jsonData(model.getJsonData())
                .build();
        // @formatter:on
    }

    @Override
    public STDWorkspaceDto convertAbridged(STDWorkspaceModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDWorkspaceDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
        // @formatter:on
    }

    @Override
    public Set<STDWorkspaceDto> convert(Set<STDWorkspaceModel> models) {
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
    public Set<STDWorkspaceDto> convertAbridged(Set<STDWorkspaceModel> models) {
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
