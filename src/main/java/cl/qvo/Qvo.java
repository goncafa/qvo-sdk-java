package cl.qvo;

import lombok.Getter;
import lombok.Setter;

public final class Qvo {
    // we do not recomend to set it true in production environment
    // if you get some SSL exception with the service endpoint we recommend
    // to manually install the certificate in the JDK
    @Setter @Getter
    private static volatile boolean ignoreUncknownSSLCertificates = false;

    // Qvo ApiToken
    @Setter @Getter
    private static volatile String apiToken;

    private Qvo() { super(); }
}