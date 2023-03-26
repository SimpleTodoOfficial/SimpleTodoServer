package de.calltopower.simpletodo.impl.requestbody;

import java.util.Set;

import de.calltopower.simpletodo.api.requestbody.STDRequestBody;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request body for a workspace
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDWorkspaceRequestBody implements STDRequestBody {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private Set<String> users;

    private String jsonData;

    @Override
    public String toString() {
        return String.format("STDWorkspaceRequestBody[name=%s, users=%s, jsonData=%s]", name, users, jsonData);
    }

}
