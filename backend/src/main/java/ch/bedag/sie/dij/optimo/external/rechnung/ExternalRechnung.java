package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.dbfiles.DbFileId;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.DossierNumberValidity;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.KesbStandortId;
import ch.bedag.sie.dij.optimo.model.RechnungId;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import ch.bedag.sie.dij.optimo.model.SozialdienstId;
import org.springframework.lang.Nullable;

/**
 * Rechnung, nachdem sie in der Datenbank im Öffentlichkeitsmodul gespeichert wurde.
 */
public record ExternalRechnung(
        RechnungId id,
        DossierNumber dossierNumber,
        @Nullable
        Iban iban,
        @Nullable
        RechnungReferenceNumber number,
        DbFileId document,
        @Nullable
        KesbStandortId kesbStandortId,
        @Nullable SozialdienstId sozialdienstId,
        DossierNumberValidity dossierNumberValidity
) {
}