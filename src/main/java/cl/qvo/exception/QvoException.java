package cl.qvo.exception;

import lombok.ToString;

@ToString(callSuper = true)
public class QvoException extends Throwable {
    public QvoException() {
    }

    public QvoException(String message) {
        super(message);
    }

    public QvoException(String message, Throwable cause) {
        super(message, cause);
    }

    public QvoException(Throwable cause) {
        super(cause);
    }

    public QvoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
