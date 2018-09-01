package cl.qvo.net.http;

import cl.qvo.net.http.exception.RestException;
import lombok.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class RestClient {
    public abstract String query(@NonNull final String endpoint) throws RestException;

    public abstract String query(@NonNull final String endpoint, Map<String,String> requestProperties) throws RestException;

    public abstract String query(@NonNull final String endpoint, String query) throws RestException;

    public abstract String query(@NonNull final String endpoint, String query, Map<String,String> requestProperties)
            throws RestException;

    public abstract String post(@NonNull final String endpoint, @NonNull final String data) throws RestException;

    public abstract String post(@NonNull final String endpoint,
                                @NonNull final String data,
                                Map<String,String> requestProperties) throws RestException;

    public abstract String put(@NonNull final String endpoint,
                      @NonNull final String data,
                      Map<String,String> requestProperties) throws RestException;

    public static Map<String, String> addJsonHeaders(Map<String, String> headers) {
        if (null == headers)
            headers = new HashMap<>();

        // json properties
        headers.put("Accept", "application/json");
        headers.put("Content-Type", String.format("%s;charset=%s", "application/json",
                StandardCharsets.UTF_8.name().toLowerCase()));

        return headers;
    }
}
