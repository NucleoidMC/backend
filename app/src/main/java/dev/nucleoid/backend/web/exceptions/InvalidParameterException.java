package dev.nucleoid.backend.web.exceptions;

public class InvalidParameterException extends WebException {
    public InvalidParameterException(String parameter) {
        super(400, "invalid value for parameter \"" + parameter + "\"");
    }

    public InvalidParameterException(String parameter, String expected) {
        super(400, "invalid value for parameter \"" + parameter + "\" (expected to be " + expected + ")");
    }
}
