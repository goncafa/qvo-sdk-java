package cl.qvo.test;

import cl.qvo.net.http.RestClient;
import cl.qvo.net.http.RestClientImpl;
import cl.qvo.net.http.exception.RestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;


@RunWith(PowerMockRunner.class)
public class RestClientTest {
    private static Logger log = LoggerFactory.getLogger(RestClientTest.class);

    @Test
    public void testPostJsonReal() throws RestException {
        RestClient restClient = RestClientImpl.getInstance();
        final String jsonOut = restClient.postJson("https://jsonplaceholder.typicode.com/users",
                "{\"name\": \"Leanne Graham\"}");
        log.debug(jsonOut);
        assertNotNull(jsonOut);
    }
}
