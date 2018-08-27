package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import cl.qvo.net.http.exception.RestException;
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
    public String postJson(@NonNull final String endpoint, @NonNull final String jsonIn) throws RestException {
        return postJson(endpoint, jsonIn, null);
    }

    public String postJson(@NonNull final String endpoint,
                           @NonNull final String jsonIn,
                           final boolean ignoreUncknownSSLCertificates) throws RestException {
        return postJson(endpoint, jsonIn, null, ignoreUncknownSSLCertificates);
    }

    public String postJson(@NonNull final String endpoint,
                           @NonNull final String jsonIn,
                           Map<String,String> requestProperties) throws RestException {
        return postJson(endpoint, jsonIn, requestProperties, false);
    }

    public String postJson(@NonNull final String endpoint,
                           @NonNull final String jsonIn,
                           Map<String,String> requestProperties,
                           final boolean ignoreUncknownSSLCertificates) throws RestException {
        try {
            return postJson(new URL(endpoint), jsonIn, requestProperties, ignoreUncknownSSLCertificates);
        } catch (MalformedURLException e) {
            throw new RestException(e);
        }
    }

    public String postJson(@NonNull final URL endpoint, @NonNull final String jsonIn) throws RestException {
        return postJson(endpoint, jsonIn, null);
    }

    public String postJson(@NonNull final URL endpoint,
                           @NonNull final String jsonIn,
                           final boolean ignoreUncknownSSLCertificates) throws RestException {
        return postJson(endpoint, jsonIn, null, ignoreUncknownSSLCertificates);
    }

    public String postJson(@NonNull final URL endpoint,
                           @NonNull final String jsonIn,
                           Map<String,String> requestProperties) throws RestException {
        return postJson(endpoint, jsonIn, requestProperties, false);
    }

    public String postJson(@NonNull final URL endpoint,
                           @NonNull final String jsonIn,
                           final Map<String,String> requestProperties,
                           final boolean ignoreUncknownSSLCertificates) throws RestException {
        final HttpURLConnection post;
        try {
            post = super.createPostConnection(endpoint, ignoreUncknownSSLCertificates);
        } catch (HttpException e) {
            throw new RestException(e);
        }

        if (null == post)
            throw new RestException(String.format("Could not create the connection with the endpoint %s", endpoint));

        // we work with UTF-8
        post.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());

        // json properties
        post.setRequestProperty("Accept", "application/json");
        post.setRequestProperty("Content-Type", String.format("%s;charset=%s", "application/json",
                StandardCharsets.UTF_8.name().toLowerCase()));

        if (null != requestProperties)
            for (Map.Entry<String, String> param : requestProperties.entrySet())
                post.setRequestProperty(param.getKey(), param.getValue());

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
                        throw new RestException(responseCode, response);

                    return response;
                }
            }
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    private static RestClientImpl instance;

    public static RestClientImpl getInstance() {
        if (null == instance)
            instance = new RestClientImpl();

        return instance;
    }
}
