package anz.core.application.shared;

public class InvalidEntityException extends RuntimeException {
    private static final long serialVersionUID = -766825400408547745L;

    public InvalidEntityException(final String message) {
        super(message);
    }
}