package dev.nucleoid.backend.web.exceptions;

public class MissingParameterException extends WebException {
    public MissingParameterException(String name) {
        super(400, "missing parameter \"" + name + '"');
    }
}
