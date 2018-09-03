package de.d3adspace.jessica.core.exception;

/**
 * Exception for the case a certain group couldn't be loaded.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(String message) {
        super(message);
    }

    public GroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
