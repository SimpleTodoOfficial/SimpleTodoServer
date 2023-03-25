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

	@Size(max = 1024)
	private String url;

	private String dueDate;

	private boolean done;

	private String jsonData;

	@Override
	public String toString() {
		// @formatter:off
        return String.format("STDTodoRequestBody[msg=%s, url=%s, dueDate=%s, jsonData=%s]",
        		msg,
        		url != null ? url : "",
        		dueDate,
        		jsonData);
    	// @formatter:on
	}

}
