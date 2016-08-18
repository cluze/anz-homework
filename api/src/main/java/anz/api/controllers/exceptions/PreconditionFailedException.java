package anz.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class PreconditionFailedException extends RuntimeException {
    private static final long serialVersionUID = 4725566183238968302L;

    public PreconditionFailedException() {
    }

    public PreconditionFailedException(final String message) {
        super(message);
    }

    public PreconditionFailedException(final Throwable throwable) {
        super(throwable);
    }

    public PreconditionFailedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}