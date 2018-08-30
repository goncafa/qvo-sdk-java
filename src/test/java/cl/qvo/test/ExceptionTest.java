package cl.qvo.test;

import cl.qvo.exception.QvoException;
import cl.qvo.net.http.exception.HttpException;
import cl.qvo.net.http.exception.RestException;
import org.junit.Test;

public class ExceptionTest {
    @Test(expected = QvoException.class)
    public void qvoExceptionTest() throws QvoException {
        throw new QvoException("test exception");
    }

    @Test(expected = HttpException.class)
    public void httpExceptionTest() throws HttpException {
        throw new HttpException("test exception");
    }

    @Test(expected = RestException.class)
    public void restExceptionTest() throws RestException {
        throw new RestException(404, "test exception");
    }
}
