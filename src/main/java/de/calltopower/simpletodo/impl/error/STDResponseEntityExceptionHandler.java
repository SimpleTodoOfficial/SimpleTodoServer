package de.calltopower.simpletodo.impl.error;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.calltopower.simpletodo.api.error.STDExceptionHandler;
import de.calltopower.simpletodo.impl.exception.STDFunctionalException;
import de.calltopower.simpletodo.impl.exception.STDGeneralException;
import de.calltopower.simpletodo.impl.exception.STDNotAuthorizedException;
import de.calltopower.simpletodo.impl.exception.STDNotFoundException;
import de.calltopower.simpletodo.impl.exception.STDUserException;

/**
 * An exception handler for all of the occurring exceptions
 */
@ControllerAdvice
@RestController
public class STDResponseEntityExceptionHandler extends ResponseEntityExceptionHandler implements STDExceptionHandler {

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(STDNotFoundException.class)
    public final ResponseEntity<STDErrorDetails> handleNotFoundException(STDNotFoundException ex, WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(STDUserException.class)
    public final ResponseEntity<STDErrorDetails> handleNotFoundException(STDUserException ex, WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(STDFunctionalException.class)
    public final ResponseEntity<STDErrorDetails> handleNotFoundException(STDFunctionalException ex,
            WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(STDNotAuthorizedException.class)
    public final ResponseEntity<STDErrorDetails> handleNotFoundException(STDNotAuthorizedException ex,
            WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(STDGeneralException.class)
    public final ResponseEntity<STDErrorDetails> handleZPAResultException(STDGeneralException ex, WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @SuppressWarnings({ "static-method", "javadoc" })
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<STDErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        STDErrorDetails errorDetails = new STDErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
