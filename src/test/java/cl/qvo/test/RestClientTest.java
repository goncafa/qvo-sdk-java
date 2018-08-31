package cl.qvo.test;

import cl.qvo.net.http.RestClient;
import cl.qvo.net.http.RestClientImpl;
import cl.qvo.net.http.exception.RestException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class RestClientTest {
    private static Logger log = LoggerFactory.getLogger(RestClientTest.class);

    @Test
    public void testPostJsonReal() throws RestException {
        RestClient restClient = RestClientImpl.getInstance();
        final String jsonOut = restClient.post("https://jsonplaceholder.typicode.com/users",
                "{\"name\": \"Leanne Graham\"}", RestClient.addJsonHeaders(null));
        log.debug(jsonOut);
        assertNotNull(jsonOut);
    }

    @Test
    public void testQuery() throws RestException {
        RestClient restClient = RestClientImpl.getInstance();
        final String response = restClient.query("http://www.google.com");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test(expected = RestException.class)
    public void testQueryUnknownHost() throws RestException {
        RestClient restClient = RestClientImpl.getInstance();
        restClient.query("http://www.anyfakerurl.goncafa");
    }
}
