package cl.qvo.net.http.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
public class RestException extends Exception {
    @Getter private final int code;

    public RestException(String message) {
        super(message);
        this.code = -1;
    }

    public RestException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RestException(String message, Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    public RestException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RestException(Throwable cause) {
        super(cause);
        this.code = -1;
    }

    public RestException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = -1;
    }

    public RestException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
}
