package cl.qvo.test;

import cl.qvo.Qvo;
import cl.qvo.model.Card;
import cl.qvo.model.Customer;
import cl.qvo.net.http.HttpChannelImpl;
import cl.qvo.net.http.exception.RestException;
import com.github.javafaker.Faker;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

public class CustomerTest {
    private Logger log = LoggerFactory.getLogger(CustomerTest.class);
    private static Faker faker;

    @BeforeClass
    public static void setup() {
        faker = Faker.instance();
        Qvo.setApiToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjb21tZXJjZV9pZCI6ImNvbV9INF82YWN3azlxMkJtN24tQUU2Y3R" +
                "BIiwiYXBpX3Rva2VuIjp0cnVlfQ.vNLWa5E6qj3SswpyNeIdSv2LA1Fv7VMjQG8bZjvmXoA");
        Qvo.setIgnoreUncknownSSLCertificates(true);
        HttpChannelImpl.setIgnoreUncknownSSLCertificates(true);
    }

    @Test
    public void testCreateLoad() throws RestException {
        Customer create = new Customer()
                .setName(faker.name().fullName())
                .setEmail(faker.internet().emailAddress())
                .setDefaultPaymentMethod(new Card());

        create.create();

        assertNotNull(create);
        assertNotNull(create.getId());

        log.debug(create.toString());
    }

    @Test
    public void testLoad() throws RestException {
        Customer customer = new Customer()
                .setId("cus_X7nvW9rFEHdm_-ib0Bds9Q");
        log.debug(customer.load().toString());
    }
}
