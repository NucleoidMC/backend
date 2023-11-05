package dev.nucleoid.backend.web.exceptions;

public class InternalServerException extends WebException {
    public InternalServerException(Throwable cause) {
        super(500, "internal server error; check the logs or contact the administrators");
        this.initCause(cause);
    }
}
