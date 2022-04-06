package ch.bedag.sie.dij.optimo.cmi;

import ch.bedag.sie.dij.optimo.model.DossierNumber;

public interface Cmi {
    /**
     * Information about a dossier given a dossier number. Will perform a CMI-call.
     */
    DossierInfoResult getDossierInfo(DossierNumber dossierNumber);
}
