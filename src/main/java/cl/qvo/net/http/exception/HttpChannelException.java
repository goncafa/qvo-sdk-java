package cl.qvo.net.http.exception;

import cl.qvo.exception.QvoException;
import lombok.ToString;

@ToString(callSuper = true)
public class HttpChannelException extends QvoException {
    public HttpChannelException() {
    }

    public HttpChannelException(String message) {
        super(message);
    }

    public HttpChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpChannelException(Throwable cause) {
        super(cause);
    }

    public HttpChannelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
