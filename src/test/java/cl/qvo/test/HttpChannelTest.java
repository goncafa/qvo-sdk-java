package cl.qvo.test;

import cl.qvo.net.http.HttpChannel;
import cl.qvo.net.http.HttpChannelImpl;
import cl.qvo.net.http.exception.HttpException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertNotNull;

public class HttpChannelTest {
    private static HttpChannel httpChannel;

    @BeforeClass
    public static void setup() {
        httpChannel = HttpChannelImpl.getInstance();
    }

    @Test
    public void testCreateGetConnection() throws HttpException {
        final HttpURLConnection httpURLConnection = httpChannel.createGetConnection("http://www.google.com");

        assertNotNull(httpURLConnection);
    }

    @Test
    public void testCreatePostConnection() throws HttpException {
        final HttpURLConnection httpURLConnection = httpChannel.createPostConnection("http://www.google.com");

        assertNotNull(httpURLConnection);
    }
}
