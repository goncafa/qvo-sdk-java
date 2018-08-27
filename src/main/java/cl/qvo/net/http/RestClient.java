package cl.qvo.net.http;

import cl.qvo.net.http.exception.RestException;
import lombok.NonNull;

import java.net.URL;
import java.util.Map;

public interface RestClient {
    String postJson(@NonNull final String endpoint, @NonNull final String jsonIn) throws RestException;

    String postJson(@NonNull final String endpoint,
                    @NonNull final String jsonIn,
                    final boolean ignoreUncknownSSLCertificates) throws RestException;

    String postJson(@NonNull final String endpoint,
                    @NonNull final String jsonIn,
                    Map<String,String> requestProperties) throws RestException;

    String postJson(@NonNull final String endpoint,
                    @NonNull final String jsonIn,
                    Map<String,String> requestProperties,
                    final boolean ignoreUncknownSSLCertificates) throws RestException;

    String postJson(@NonNull final URL endpoint, @NonNull final String jsonIn) throws RestException;

    String postJson(@NonNull final URL endpoint,
                    @NonNull final String jsonIn,
                    final boolean ignoreUncknownSSLCertificates) throws RestException;

    String postJson(@NonNull final URL endpoint,
                    @NonNull final String jsonIn,
                    Map<String,String> requestProperties) throws RestException;

    String postJson(@NonNull final URL endpoint,
                           @NonNull final String jsonIn,
                           final Map<String,String> requestProperties,
                           final boolean ignoreUncknownSSLCertificates) throws RestException;
}
