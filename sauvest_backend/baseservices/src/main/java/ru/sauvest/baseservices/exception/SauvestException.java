package ru.sauvest.baseservices.exception;

import lombok.Getter;

@Getter
public class SauvestException extends RuntimeException {

    protected final String code;

    public SauvestException(String code) {
        this.code = code;
    }

    public SauvestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SauvestException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
