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
 * Request body for a todo movement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDTodoMovementRequestBody implements STDRequestBody {

    @NotBlank
    @Size(min = 30, max = 50)
    private String listId;

    @Override
    public String toString() {
        return String.format("STDTodoMovementRequestBody[listId=%s]", listId);
    }

}
