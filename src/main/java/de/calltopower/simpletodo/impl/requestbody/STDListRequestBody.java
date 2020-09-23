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
 * Request body for a list
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDListRequestBody implements STDRequestBody {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    private String jsonData;

    @Override
    public String toString() {
        return String.format("STDListRequestBody[name=%s, jsonData=%s]", name, jsonData);
    }

}
