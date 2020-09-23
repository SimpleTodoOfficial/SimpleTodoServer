package de.calltopower.simpletodo.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.calltopower.simpletodo.api.exception.STDException;

/**
 * "Not Found" exception
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class STDNotFoundException extends RuntimeException implements STDException {

    private static final long serialVersionUID = -1932271206882271621L;

    @SuppressWarnings("javadoc")
    public STDNotFoundException() {
        super();
    }

    @SuppressWarnings("javadoc")
    public STDNotFoundException(String msg) {
        super(msg);
    }

    @SuppressWarnings("javadoc")
    public STDNotFoundException(Exception e) {
        super(e);
    }

}
