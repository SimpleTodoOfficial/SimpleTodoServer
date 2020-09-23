package de.calltopower.simpletodo.impl.requestbody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.calltopower.simpletodo.api.requestbody.STDRequestBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request body for a todo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDTodoRequestBody implements STDRequestBody {

    @NotBlank
    @Size(max = 1024)
    private String msg;

    private String dueDate;

    private boolean done;

    private String jsonData;

    @Override
    public String toString() {
        return String.format("STDTodoRequestBody[msg=%s, dueDate=%s, jsonData=%s]", msg, dueDate, jsonData);
    }

}
