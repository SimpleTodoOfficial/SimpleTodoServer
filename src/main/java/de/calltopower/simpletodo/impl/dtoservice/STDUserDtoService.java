package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDUserDto;
import de.calltopower.simpletodo.impl.model.STDRoleModel;
import de.calltopower.simpletodo.impl.model.STDUserModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the user DTO
 */
@NoArgsConstructor
@Service
public class STDUserDtoService implements STDDtoService<STDUserDto, STDUserModel> {

    @Override
    public STDUserDto convert(STDUserModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDUserDto.builder()
                .id(model.getId())
                .username(model.getUsername())
                .email(model.getEmail())
                .statusActivated(model.isStatusActivated())
                .roles(getRoleNames(model.getRoles()))
                .jsonData(model.getJsonData())
                .build();
        // @formatter:on
    }

    @Override
    public STDUserDto convertAbridged(STDUserModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        return STDUserDto.builder()
                .id(model.getId())
                .username(model.getUsername())
                .build();
        // @formatter:on
    }

    @Override
    public Set<STDUserDto> convert(Set<STDUserModel> models) {
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
    public Set<STDUserDto> convertAbridged(Set<STDUserModel> models) {
        if (models == null) {
            return new HashSet<>();
        }

        // @formatter:off
        return models.stream()
                        .map(m -> convertAbridged(m))
                        .collect(Collectors.toSet());
        // @formatter:on
    }

    private Set<String> getRoleNames(Set<STDRoleModel> roles) {
        return roles.stream().map(role -> role.getName().getInternalName()).collect(Collectors.toSet());
    }

}
