package ch.bedag.sie.dij.optimo.cmi.simulator;

import ch.bedag.sie.dij.optimo.cmi.Cmi;
import ch.bedag.sie.dij.optimo.cmi.DossierInfoResult;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// TODO: Könnte auch in den test-bereich verschoben werden, muss nicht zwingend im produktivcode sein.
@Service
public class CmiSimulator implements Cmi {

    private final Map<DossierNumber, DossierInfoResult> dossierInfoResults = new HashMap<>();
    private DossierInfoResult defaultDossierInfoResult = new DossierInfoResult.BackendNotAvailable();

    @Override
    public DossierInfoResult getDossierInfo(DossierNumber dossierNumber) {
        var maybeDossierInfoResult = this.dossierInfoResults.get(dossierNumber);
        if (maybeDossierInfoResult == null) {
            return defaultDossierInfoResult;
        } else {
            return maybeDossierInfoResult;
        }
    }

    public Map<DossierNumber, DossierInfoResult> getDossierInfoResults() {
        return dossierInfoResults;
    }

    public void setDefaultDossierInfoResult(DossierInfoResult defaultDossierInfoResult) {
        this.defaultDossierInfoResult = defaultDossierInfoResult;
    }
}
