package de.calltopower.simpletodo.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.calltopower.simpletodo.api.exception.STDException;

/**
 * "Not authorized" exception
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class STDNotAuthorizedException extends RuntimeException implements STDException {

    private static final long serialVersionUID = -5490671963188802180L;

    @SuppressWarnings("javadoc")
    public STDNotAuthorizedException() {
        super();
    }

    @SuppressWarnings("javadoc")
    public STDNotAuthorizedException(String msg) {
        super(msg);
    }

    @SuppressWarnings("javadoc")
    public STDNotAuthorizedException(Exception e) {
        super(e);
    }

}
