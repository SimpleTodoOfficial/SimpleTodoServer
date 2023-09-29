package de.calltopower.simpletodo.impl.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDWorkspaceModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for a workspace model
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDWorkspaceDto implements STDDto<STDWorkspaceModel> {

    @JsonProperty
    public UUID id;

    @JsonProperty
    public Date createdDate;

    @JsonProperty
    public String name;

    @JsonProperty
    public Set<STDUserDto> users;

    @JsonProperty
    public Set<STDListDto> lists;

    @JsonProperty
    private String jsonData;

}
