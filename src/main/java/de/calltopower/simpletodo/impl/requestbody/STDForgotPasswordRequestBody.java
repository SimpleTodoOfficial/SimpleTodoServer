package de.calltopower.simpletodo.impl.requestbody;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.calltopower.simpletodo.api.requestbody.STDRequestBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request body for a forgot password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class STDForgotPasswordRequestBody implements STDRequestBody {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    public String email;

    @Override
    public String toString() {
        return String.format("STDForgotPasswordRequestBody[username=%s, email=%s]", username, email);
    }

}
