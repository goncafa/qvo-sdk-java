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
import lombok.NonNull;
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

    Model post(Object data) throws RestException {
        String url = String.format("%s/%s", getEnvironment(), getEndpoint());

        final Map<String, String> qvoHeaders = createQvoAuthorizationHeader();
        RestClient.addJsonHeaders(qvoHeaders);

        String jsonOut = getRestClient().post(url, gson.toJson(data), qvoHeaders);

        copy(gson.fromJson(jsonOut, getClass()));

        return this;
    }

    Model get() throws RestException {
        String url = String.format("%s/%s/%s", getEnvironment(), getEndpoint(), getId());
        final String jsonOut = getRestClient().query(url, createQvoAuthorizationHeader());

        copy(gson.fromJson(jsonOut, getClass()));

        return this;
    }

    Model update() {

        return null;
    }

    private ApiEnvironment getEnvironment() {
        if (null == Qvo.getApiEnvironment())
            return ApiEnvironment.SANDBOX;

        return Qvo.getApiEnvironment();
    }

    private Map<String, String> createQvoAuthorizationHeader() {
        return addQvoAuthorizationHeader(new HashMap<String, String>());
    }

    private Map<String, String> addQvoAuthorizationHeader(@NonNull Map<String, String> headers) {
        // set qvo authorization header
        headers.put("Authorization",  String.format("Bearer %s", Qvo.getApiToken()));

        return headers;
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

    abstract String getId();

    abstract String getEndpoint();

    abstract Model create() throws RestException;
}
