package de.calltopower.simpletodo.impl.dtoservice;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.calltopower.simpletodo.api.dtoservice.STDDtoService;
import de.calltopower.simpletodo.impl.dto.STDTokenDto;
import de.calltopower.simpletodo.impl.dto.STDUserDto;
import de.calltopower.simpletodo.impl.model.STDTokenModel;
import lombok.NoArgsConstructor;

/**
 * DTO service implementation for the token DTO
 */
@NoArgsConstructor
@Service
public class STDTokenDtoService implements STDDtoService<STDTokenDto, STDTokenModel> {

    private STDUserDtoService userDtoService;

    @Autowired
    public STDTokenDtoService(STDUserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @Override
    public STDTokenDto convert(STDTokenModel model) {
        if (model == null) {
            return null;
        }

        // @formatter:off
        STDUserDto user = userDtoService.convert(model.getUser());
        return STDTokenDto.builder()
                .token(model.getToken())
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
        // @formatter:on
    }

    @Override
    public STDTokenDto convertAbridged(STDTokenModel model) {
        return convert(model);
    }

    @Override
    public Set<STDTokenDto> convert(Set<STDTokenModel> models) {
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
    public Set<STDTokenDto> convertAbridged(Set<STDTokenModel> models) {
        return convert(models);
    }

}
