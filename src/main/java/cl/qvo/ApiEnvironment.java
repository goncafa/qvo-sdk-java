package cl.qvo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApiEnvironment {
    SANDBOX("https://playground.qvo.cl"),
    PRODUCTION("https://api.qvo.cl");

    @Getter private String baseUrl;
}
