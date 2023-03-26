package de.calltopower.simpletodo.impl.requestbody;

import de.calltopower.simpletodo.api.requestbody.STDRequestBody;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request body for a list movement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDListMovementRequestBody implements STDRequestBody {

    @NotBlank
    @Size(min = 30, max = 50)
    private String workspaceId;

    @Override
    public String toString() {
        return String.format("STDListMovementRequestBody[workspaceId=%s]", workspaceId);
    }

}
