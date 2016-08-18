package anz.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -5272679701090110717L;

    public BadRequestException() {
    }

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final Throwable throwable) {
        super(throwable);
    }

    public BadRequestException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}