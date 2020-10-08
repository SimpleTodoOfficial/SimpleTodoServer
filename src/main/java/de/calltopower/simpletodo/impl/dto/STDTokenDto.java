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

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String token;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID id;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String username;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String email;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public Boolean statusActivated;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public Set<String> roles;

}
