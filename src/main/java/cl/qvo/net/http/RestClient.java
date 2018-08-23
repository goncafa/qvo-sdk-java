package cl.qvo.net.http;

import cl.qvo.net.http.exception.RestClientException;
import lombok.NonNull;

import java.net.URL;
import java.util.Map;

public interface RestClient {
    String postJson(@NonNull final String endpoint, @NonNull final String jsonIn) throws RestClientException;

    String postJson(@NonNull final String endpoint,
                    @NonNull final String jsonIn,
                    Map<String,String> requestProperties) throws RestClientException;

    String postJson(@NonNull final URL endpoint, @NonNull final String jsonIn) throws RestClientException;

    String postJson(@NonNull final URL endpoint,
                    @NonNull final String jsonIn,
                    Map<String,String> requestProperties) throws RestClientException;
}
