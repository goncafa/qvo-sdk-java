package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import cl.qvo.net.http.exception.RestException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class RestClientImpl extends RestClient {
    @Getter @Setter
    private HttpChannel httpChannel = HttpChannelImpl.getInstance();

    public String query(@NonNull final String endpoint) throws RestException {
        return query(endpoint, (String) null);
    }

    public String query(@NonNull final String endpoint, Map<String,String> requestProperties) throws RestException {
        return query(endpoint,null, requestProperties);
    }

    public String query(@NonNull final String endpoint, String query) throws RestException {
        return query(endpoint, query, null);
    }

    public String query(@NonNull final String endpoint, String query, Map<String,String> requestProperties) throws RestException {
        final HttpURLConnection get;
        try {
            get = httpChannel.createGetConnection(endpoint, query);

            if (null != requestProperties)
                for (Map.Entry<String, String> param : requestProperties.entrySet())
                    get.setRequestProperty(param.getKey(), param.getValue());

            final int responseCode = get.getResponseCode();

            // read response data
            // if response code is < HttpURLConnection.HTTP_BAD_REQUEST read the InputStream
            // else response come on ErrorStream
            boolean isOk = responseCode < HttpURLConnection.HTTP_BAD_REQUEST;
            return readBody(get, responseCode, isOk);
        } catch (IOException | HttpException e) {
            throw new RestException(e);
        }
    }

    public String post(@NonNull final String endpoint, @NonNull final String data) throws RestException {
        return post(endpoint, data, null);
    }

    public String post(@NonNull final String endpoint,
                       @NonNull final String data,
                       Map<String,String> requestProperties) throws RestException {
        final HttpURLConnection post;
        try {
            post = getHttpChannel().createPostConnection(endpoint);
        } catch (HttpException e) {
            throw new RestException(e);
        }

        return doCall(post, data, requestProperties);
    }

    public String put(@NonNull final String endpoint,
                      @NonNull final String data,
                      Map<String,String> requestProperties) throws RestException {
        final HttpURLConnection put;
        try {
            put = getHttpChannel().createPutConnection(endpoint);
        } catch (HttpException e) {
            throw new RestException(e);
        }

        return doCall(put, data, requestProperties);
    }

    private String doCall(@NonNull HttpURLConnection connection,
                          @NonNull final String data,
                          Map<String,String> requestProperties)
            throws RestException{
        // we work with UTF-8
        connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());

        if (null != requestProperties)
            for (Map.Entry<String, String> param : requestProperties.entrySet())
                connection.setRequestProperty(param.getKey(), param.getValue());

        // post json
        try (final OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));

            // get response code
            final int responseCode = connection.getResponseCode();

            // read response data
            // if response code is < HttpURLConnection.HTTP_BAD_REQUEST read the InputStream
            // else response come on ErrorStream
            boolean isOk = responseCode < HttpURLConnection.HTTP_BAD_REQUEST;
            return readBody(connection, responseCode, isOk);
        } catch (IOException e) {
            throw new RestException(e);
        }
    }

    private String readBody(HttpURLConnection post, int responseCode, boolean isOk) throws IOException, RestException {
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
    }

    private static RestClientImpl instance;

    public static RestClientImpl getInstance() {
        if (null == instance)
            instance = new RestClientImpl();

        return instance;
    }
}
