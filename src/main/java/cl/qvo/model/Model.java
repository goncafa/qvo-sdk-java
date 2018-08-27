package cl.qvo.model;

import cl.qvo.ApiEnvironment;
import cl.qvo.Qvo;
import cl.qvo.net.http.RestClient;
import cl.qvo.net.http.RestClientImpl;
import cl.qvo.net.http.exception.RestException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

abstract class Model {
    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PUBLIC)
    private RestClient restClient;

    private Gson gson;

    Model() {
        super();
        setRestClient(RestClientImpl.getInstance());

        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    Model post() throws RestException {
        ApiEnvironment apiEnvironment = Qvo.getApiEnvironment();
        if (null == apiEnvironment)
            apiEnvironment = ApiEnvironment.SANDBOX;

        // set qvo authorization header
        Map<String, String> qvoHeaders = new HashMap<>();
        qvoHeaders.put("Authorization",  String.format("Bearer %s", Qvo.getApiToken()));

        String url = String.format("%s/%s", apiEnvironment, getEndpoint());
        String jsonOut = getRestClient().postJson(url, gson.toJson(this), qvoHeaders, Qvo.isIgnoreUncknownSSLCertificates());

        final Class<? extends Model> type = getClass();
        copy(gson.fromJson(jsonOut, type));

        return this;
    }

    private void copy(Model origin) {
        final Class<? extends Model> type = getClass();
        final Method[] methods = type.getDeclaredMethods();
        for (Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("get")) {
                final String setterName = methodName.replaceFirst("get", "set");
                try {
                    final Method setterMethod = type.getMethod(setterName, method.getReturnType());
                    final Object value = method.invoke(origin);
                    if (null != value)
                        setterMethod.invoke(this, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // do nothing
                }
            }
        }
    }

    abstract String getEndpoint();

    abstract Model create() throws RestException;
}
