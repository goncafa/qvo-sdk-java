package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import lombok.NonNull;

import java.net.HttpURLConnection;

public interface HttpChannel {
    HttpURLConnection createGetConnection(@NonNull String endpoint) throws HttpException;

    HttpURLConnection createGetConnection(@NonNull String endpoint, String query) throws HttpException;

    HttpURLConnection createPostConnection(@NonNull final String endpoint) throws HttpException;

    HttpURLConnection createConnection(@NonNull final String endpoint,
                                       @NonNull final HttpRequestMethod method) throws HttpException;
}
