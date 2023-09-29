package de.calltopower.simpletodo.impl.dto;

import java.util.Date;
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

    @JsonProperty
    public UUID id;

    @JsonProperty
    public Date createdDate;

    @JsonProperty
    public String msg;

    @JsonProperty
    public String url;

    @JsonProperty
    public Boolean done;

    @JsonProperty
    public UUID workspaceId;

    @JsonProperty
    public String workspaceName;

    @JsonProperty
    public UUID listId;

    @JsonProperty
    public String listName;

    @JsonProperty
    public Date dueDate;

    @JsonProperty
    private String jsonData;

}
