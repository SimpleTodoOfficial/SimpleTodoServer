package de.calltopower.simpletodo.impl.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.calltopower.simpletodo.api.dto.STDDto;
import de.calltopower.simpletodo.impl.model.STDTodoModel;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for a todo model
 */
@Data
@Builder
@JsonInclude(Include.NON_EMPTY)
public class STDTodoDto implements STDDto<STDTodoModel> {

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID id;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String createdDate;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String msg;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public Boolean done;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID workspaceId;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String workspaceName;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public UUID listId;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String listName;

    @SuppressWarnings("javadoc")
    @JsonProperty
    public String dueDate;

    @SuppressWarnings("javadoc")
    @JsonProperty
    private String jsonData;

}
