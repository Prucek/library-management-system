package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class representing an object not found exception.
 *
 * @author Juraj Marcin
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Creates a new NotFound exception.
     */
    public NotFoundException() {
    }

    /**
     * Creates a new NotFound exception with an error description.
     *
     * @param message error description
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new NotFound exception with an error description and a root cause.
     *
     * @param message error description
     * @param cause   root cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new NotFound exception a root cause.
     *
     * @param cause root cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
