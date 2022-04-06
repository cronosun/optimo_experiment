package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.dbfiles.DbFileId;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;

import org.springframework.lang.Nullable;

/**
 * Rechnung, so wie vom Benutzer im Öffentlichkeitsmodul eingegeben.
 */
public record EnteredRechnung(
        @Nullable
        DossierNumber dossierNumber,
        @Nullable Iban iban,
        @Nullable RechnungReferenceNumber number,
        @Nullable
        DbFileId document) {
}
