package cl.qvo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApiEnvironment {
    SANDBOX("https://playground.qvo.cl"),
    PRODUCTION("https://api.qvo.cl");

    @Getter(AccessLevel.PRIVATE) private String baseUrl;

    @Override
    public String toString() {
        return getBaseUrl();
    }
}
