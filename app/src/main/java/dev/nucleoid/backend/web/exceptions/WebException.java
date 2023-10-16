package dev.nucleoid.backend.web.exceptions;

public abstract class WebException extends RuntimeException {
    private final int status;
    private final String message;

    protected WebException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
