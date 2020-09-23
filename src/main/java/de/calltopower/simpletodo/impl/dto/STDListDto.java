package de.calltopower.simpletodo.impl.dto;

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

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID id;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String createdDate;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String name;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID workspaceId;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String workspaceName;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public Set<STDTodoDto> todos;

    @SuppressWarnings("javadoc")
    @JsonProperty
    private String jsonData;

}
