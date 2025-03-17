package ru.otus.auth.exception;

import lombok.Getter;

@Getter
public class SauvestAuthException extends RuntimeException {

    private final String code;

    public SauvestAuthException(String code) {
        this.code = code;
    }

    public SauvestAuthException(String code, String message) {
        super(message);
        this.code = code;
    }
}
