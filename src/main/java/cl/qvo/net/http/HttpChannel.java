package cl.qvo.net.http;

import cl.qvo.net.http.exception.HttpException;
import lombok.NonNull;

import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpChannel {
    HttpURLConnection createPostConnection(@NonNull final URL endpoint) throws HttpException;

    HttpURLConnection createPostConnection(@NonNull final URL endpoint,
                                           final boolean ignoreUncknownSSLCertificates) throws HttpException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean useCaches,
                                       final boolean doInput,
                                       final boolean doOutput) throws HttpException;

    HttpURLConnection createConnection(@NonNull final URL endpoint,
                                       @NonNull final HttpRequestMethod method,
                                       final boolean useCaches,
                                       final boolean doInput,
                                       final boolean doOutput,
                                       final boolean ignoreUncknownSSLCertificates) throws HttpException;
}
