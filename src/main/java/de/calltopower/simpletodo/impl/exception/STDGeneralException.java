package de.calltopower.simpletodo.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.calltopower.simpletodo.api.exception.STDException;

/**
 * "General" exception
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class STDGeneralException extends RuntimeException implements STDException {

    private static final long serialVersionUID = 7422448735206699827L;

    public STDGeneralException() {
        super();
    }

    public STDGeneralException(String msg) {
        super(msg);
    }

    public STDGeneralException(Exception e) {
        super(e);
    }

}
