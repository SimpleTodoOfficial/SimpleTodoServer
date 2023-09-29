package de.calltopower.simpletodo.impl.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDListModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for a list model
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDListDto implements STDDto<STDListModel> {

    @JsonProperty
    public UUID id;

    @JsonProperty
    public Date createdDate;

    @JsonProperty
    public String name;

    @JsonProperty
    public UUID workspaceId;

    @JsonProperty
    public String workspaceName;

    @JsonProperty
    public Set<STDTodoDto> todos;

    @JsonProperty
    private String jsonData;

}
