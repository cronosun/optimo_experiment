package ch.bedag.sie.dij.optimo.internal.rechnung;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocumentId;
import ch.bedag.sie.dij.optimo.model.RechnungId;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.DossierNumberValidity;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.KesbStandortId;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import ch.bedag.sie.dij.optimo.model.RechnungStatus;
import ch.bedag.sie.dij.optimo.model.SozialdienstId;
import org.springframework.lang.Nullable;

/**
 * Rechnung wie sie im internen modul ("dabbawala") verwendet wird; nachdem sie vom externen modul importiert wurde.
 * <p>
 * Wäre in realtiät deutlich grösser, siehe: https://confluence.bedag.ch/display/DIJ/Rechnungen
 */
public record InternalRechnung(
        RechnungId id,
        DossierNumber dossierNumber,
        @Nullable
        Iban iban,
        @Nullable
        RechnungReferenceNumber number,
        DabDocumentId document,
        @Nullable
        KesbStandortId kesbStandortId,
        @Nullable SozialdienstId sozialdienstId,
        DossierNumberValidity dossierNumberValidity,
        RechnungStatus status
) {
    public InternalRechnung withStatus(RechnungStatus status) {
        return new InternalRechnung(id, dossierNumber, iban, number, document, kesbStandortId, sozialdienstId, dossierNumberValidity, status);
    }
}
