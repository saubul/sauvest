package ru.otus.auth.exception;

public final class ExceptionCodeConstants {

    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";

    public static final String WRONG_AUTHORIZATION_HEADER = "WRONG_AUTHORIZATION_HEADER";

    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    public static final String INVALID_TOKEN = "INVALID_TOKEN";

    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    public static final String AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";

    private ExceptionCodeConstants() {}
}
