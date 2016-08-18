package anz.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7109600609476461522L;

    public NotFoundException() {
    }

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final Throwable throwable) {
        super(throwable);
    }

    public NotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}