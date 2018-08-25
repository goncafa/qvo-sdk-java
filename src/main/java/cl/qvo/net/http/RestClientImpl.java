package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpChannelException;
import cl.qvo.net.http.exception.RestClientException;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class RestClientImpl extends HttpChannelImpl implements RestClient {
    public String postJson(@NonNull final String endpoint, @NonNull final String jsonIn) throws RestClientException {
        return postJson(endpoint, jsonIn, null);
    }

    public String postJson(@NonNull final String endpoint,
                           @NonNull final String jsonIn,
                           Map<String,String> requestProperties) throws RestClientException {
        try {
            return postJson(new URL(endpoint), jsonIn, requestProperties);
        } catch (MalformedURLException e) {
            throw new RestClientException(e);
        }
    }

    public String postJson(@NonNull final URL endpoint, @NonNull final String jsonIn) throws RestClientException {
        return postJson(endpoint, jsonIn, null);
    }

    public String postJson(@NonNull final URL endpoint,
                           @NonNull final String jsonIn,
                           Map<String,String> requestProperties) throws RestClientException {
        final HttpURLConnection post;
        try {
            post = super.createPostConnection(endpoint);
        } catch (HttpChannelException e) {
            throw new RestClientException(e);
        }

        if (null == post)
            throw new RestClientException(String.format("Could not create the connection with the endpoint %s", endpoint));

        // we work with UTF-8
        post.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());

        // json properties
        post.setRequestProperty("Accept", "application/json");
        post.setRequestProperty("Content-Type", String.format("%s;charset=%s", "application/json",
                StandardCharsets.UTF_8.name().toLowerCase()));

        // post json
        try (final OutputStream outputStream = post.getOutputStream()) {
            outputStream.write(jsonIn.getBytes(StandardCharsets.UTF_8));

            // get response code
            final int responseCode = post.getResponseCode();

            // read response data
            // if response code is < HttpURLConnection.HTTP_BAD_REQUEST read the InputStream
            // else response come on ErrorStream
            boolean isOk = responseCode < HttpURLConnection.HTTP_BAD_REQUEST;
            try (final InputStream inputStream = (isOk) ?
                    post.getInputStream() :
                    post.getErrorStream()) {
                try (final Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                    String response = scanner.useDelimiter("\\A").next();

                    if (!isOk)
                        throw new RestClientException(responseCode, response);

                    return response;
                }
            }
        } catch (IOException e) {
            throw new RestClientException(e);
        }
    }

    private static RestClientImpl instance;

    public static RestClientImpl getInstance() {
        if (null == instance)
            instance = new RestClientImpl();

        return instance;
    }
}
