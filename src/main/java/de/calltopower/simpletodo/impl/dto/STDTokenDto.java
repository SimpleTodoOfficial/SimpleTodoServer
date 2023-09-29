package de.calltopower.simpletodo.impl.dto;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDTokenModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for a token model
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDTokenDto implements STDDto<STDTokenModel> {

    @JsonProperty
    public String token;

    @JsonProperty
    public UUID id;

    @JsonProperty
    public String username;

    @JsonProperty
    public String email;

    @JsonProperty
    public Boolean statusVerified;

    @JsonProperty
    public Set<String> roles;

}
