package de.calltopower.simpletodo.api.error;

import java.util.Date;

/**
 * An error
 */
public interface STDError {

    /**
     * Returns the timestamp of the error
     * 
     * @return The timestamp
     */
    default Date getTimestamp() {
        return new Date();
    }

    /**
     * Returns the message of the error
     * 
     * @return The message
     */
    String getMessage();

    /**
     * Returns the details of the error
     * 
     * @return The details
     */
    String getDetails();

}
