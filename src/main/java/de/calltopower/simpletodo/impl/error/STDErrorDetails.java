package de.calltopower.simpletodo.impl.error;

import java.util.Date;

import de.calltopower.simpletodo.api.error.STDError;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An error details object that is sent as an answer to erroneous requests
 */
@Getter
@AllArgsConstructor
public class STDErrorDetails implements STDError {

    private Date timestamp;
    private String message;
    private String details;

}
