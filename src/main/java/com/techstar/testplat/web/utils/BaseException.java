package com.techstar.testplat.web.utils;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -3545171517753817295L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
