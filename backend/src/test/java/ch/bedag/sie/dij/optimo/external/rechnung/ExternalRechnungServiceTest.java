package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.cmi.DossierInfoResult;
import ch.bedag.sie.dij.optimo.cmi.simulator.CmiSimulator;
import ch.bedag.sie.dij.optimo.dbfiles.DbFileId;
import ch.bedag.sie.dij.optimo.dbfiles.DbFilesTestService;
import ch.bedag.sie.dij.optimo.exception.ValidationExceptionChecker;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class ExternalRechnungServiceTest {

    @Autowired
    private ExternalRechnungService externalRechnungService;
    @Autowired
    private CmiSimulator cmiSimulator;
    @Autowired
    private DbFilesTestService dbFilesTestService;

    /**
     * https://confluence.bedag.ch/display/DIJ/UC02+Rechnung+einliefern
     */
    @Test
    void uc02_enterValidRechnung_succeeds() {
        // rechnung wie vom benutzer im externen zugang eingegeben
        var dossierNumber = new DossierNumber("e2022.kbl.22");
        var iban = new Iban("CH7985545454564564");
        var number = new RechnungReferenceNumber("654654654564564654687787");
        var document = dbFilesTestService.saveAnyFile();
        var enteredRechnung = new EnteredRechnung(dossierNumber, iban, number, document);

        // rechnung "speichern"
        externalRechnungService.saveUserEnteredRechnung(enteredRechnung);
    }

    @Test
    void uc02_enterRechnungWithMissingFields_missingFieldsAreMarked() {
        // Laut spezifikation (https://confluence.bedag.ch/display/DIJ/UC02+Rechnung+einliefern) sind folgende Felder zwingend:

        // - dossier-number
        // - rechnungs-dokument

        var iban = new Iban("CH7985545454564564");
        var number = new RechnungReferenceNumber("654654654564564654687787");
        var enteredRechnung = new EnteredRechnung(null, iban, number, null);

        // rechnung "speichern": Haben wir alle validations-fehler?
        ValidationExceptionChecker.assertHasExactlyGivenValidationErrors(() -> externalRechnungService.saveUserEnteredRechnung(enteredRechnung),
                ExternalRechnungService.VALIDATION_MISSING_RECHNUNG_DOCUMENT,
                ExternalRechnungService.VALIDATION_MISSING_DOSSIER_NUMBER);
    }

    @Test
    void uc02_enterRechnungWithNoUploadedDocument_missingDocumentIsReported() {
        // rechnung wie vom benutzer im externen zugang eingegeben
        var dossierNumber = new DossierNumber("e2022.kbl.22");
        var iban = new Iban("CH7985545454564564");
        var number = new RechnungReferenceNumber("654654654564564654687787");
        // hier ist ein dokument angegeben, was es nicht gibt.
        var document = new DbFileId(UUID.randomUUID());
        var enteredRechnung = new EnteredRechnung(dossierNumber, iban, number, document);

        // rechnung "speichern": Sollte es anzeigen, falls das Rechnungs-Dokument nicht hochgeladen wurde.
        ValidationExceptionChecker.assertHasExactlyGivenValidationErrors(() -> externalRechnungService.saveUserEnteredRechnung(enteredRechnung),
                ExternalRechnungService.VALIDATION_RECHNUNG_DOCUMENT_NOT_PERSISTED);
    }

    @Test
    void uc02_enterRechnungWithDossierNumberThatDoesNotExist_errorIsReported() {
        var testDossierNumber = new DossierNumber("e2022.kbl.22");
        // configure the CMI simulator: Dossier does not exist
        cmiSimulator.getDossierInfoResults().put(testDossierNumber, new DossierInfoResult.NoSuchNumber());

        // rechnung wie vom benutzer im externen zugang eingegeben
        var iban = new Iban("CH7985545454564564");
        var number = new RechnungReferenceNumber("654654654564564654687787");
        var document = dbFilesTestService.saveAnyFile();
        var enteredRechnung = new EnteredRechnung(testDossierNumber, iban, number, document);

        // rechnung "speichern": Sollte zurück liefern: ungültige dossier-number
        ValidationExceptionChecker.assertHasExactlyGivenValidationErrors(() -> externalRechnungService.saveUserEnteredRechnung(enteredRechnung),
                ExternalRechnungService.VALIDATION_INVALID_DOSSIER_NUMBER);
    }
}