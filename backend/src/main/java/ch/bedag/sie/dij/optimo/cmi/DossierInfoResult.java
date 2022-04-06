package ch.bedag.sie.dij.optimo.cmi;

import ch.bedag.sie.dij.optimo.model.KesbStandort;
import ch.bedag.sie.dij.optimo.model.Sozialdienst;
import lombok.EqualsAndHashCode;
import lombok.Value;

public class DossierInfoResult {

    private DossierInfoResult() {
    }

    public static final class NoSuchNumber extends DossierInfoResult {
    }

    public static final class BackendNotAvailable extends DossierInfoResult {
    }

    @EqualsAndHashCode(callSuper = false)
    @Value
    public static class Found extends DossierInfoResult {
        KesbStandort standort;
        Sozialdienst sozialdienst;
    }
}
