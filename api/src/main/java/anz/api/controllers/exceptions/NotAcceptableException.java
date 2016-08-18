package anz.api.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class NotAcceptableException extends RuntimeException {
    private static final long serialVersionUID = 2717978003486346577L;

    public NotAcceptableException() {
    }

    public NotAcceptableException(final String message) {
        super(message);
    }

    public NotAcceptableException(final Throwable throwable) {
        super(throwable);
    }

    public NotAcceptableException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}