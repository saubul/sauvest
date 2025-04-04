package ru.sauvest.auth.exception;

import lombok.Getter;
import ru.sauvest.baseservices.exception.SauvestException;

@Getter
public class SauvestAuthException extends SauvestException {

    public SauvestAuthException(String code) {
        super(code);
    }

    public SauvestAuthException(String code, String message) {
        super(code, message);
    }

    public SauvestAuthException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
