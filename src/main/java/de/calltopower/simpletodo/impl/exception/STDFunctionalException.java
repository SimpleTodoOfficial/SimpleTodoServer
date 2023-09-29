package de.calltopower.simpletodo.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.calltopower.simpletodo.api.exception.STDException;

/**
 * "Functional" exception
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class STDFunctionalException extends RuntimeException implements STDException {

    private static final long serialVersionUID = 5950759560263566315L;

    public STDFunctionalException() {
        super();
    }

    public STDFunctionalException(String msg) {
        super(msg);
    }

    public STDFunctionalException(Exception e) {
        super(e);
    }

}
