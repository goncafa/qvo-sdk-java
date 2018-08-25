package cl.qvo.net.http.exception;

import lombok.Getter;

public class RestClientException extends Exception {
    @Getter private final int code;

    public RestClientException(String message) {
        super(message);
        this.code = -1;
    }

    public RestClientException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RestClientException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    public RestClientException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RestClientException(Throwable cause) {
        super(cause);
        this.code = -1;
    }

    public RestClientException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public RestClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = -1;
    }

    public RestClientException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
