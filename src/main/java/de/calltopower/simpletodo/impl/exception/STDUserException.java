package de.calltopower.simpletodo.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.calltopower.simpletodo.api.exception.STDException;

/**
 * "User" exception
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class STDUserException extends RuntimeException implements STDException {

    private static final long serialVersionUID = -3633741342985442301L;

    @SuppressWarnings("javadoc")
    public STDUserException() {
        super();
    }

    @SuppressWarnings("javadoc")
    public STDUserException(String msg) {
        super(msg);
    }

    @SuppressWarnings("javadoc")
    public STDUserException(Exception e) {
        super(e);
    }

}
