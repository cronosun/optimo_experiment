package ch.bedag.sie.dij.optimo.internal.rechnung;

import ch.bedag.sie.dij.optimo.dabbawala.Dabbawala;
import ch.bedag.sie.dij.optimo.dabbawala.simulator.DabbawalaSimulatorService;
import ch.bedag.sie.dij.optimo.dbfiles.DbFilesService;
import ch.bedag.sie.dij.optimo.dbfiles.DbFilesTestService;
import ch.bedag.sie.dij.optimo.exception.ValidationExceptionChecker;
import ch.bedag.sie.dij.optimo.external.rechnung.EnteredRechnung;
import ch.bedag.sie.dij.optimo.external.rechnung.ExternalRechnungService;
import ch.bedag.sie.dij.optimo.importer.ImporterService;
import ch.bedag.sie.dij.optimo.mail.simulator.MailSenderSimulator;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.KnownDabRoles;
import ch.bedag.sie.dij.optimo.model.RechnungId;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.constraints.NotNull;

@SpringBootTest
class InternalRechnungServiceTest {

    @Autowired
    private ExternalRechnungService externalRechnungService;
    @Autowired
    private DbFilesTestService dbFilesTestService;
    @Autowired
    private ImporterService importerService;
    @Autowired
    private InternalRechnnungService internalRechnnungService;
    @Autowired
    private Dabbawala dabbawala;
    @Autowired
    private DbFilesService dbFilesService;
    @Autowired
    private DabbawalaSimulatorService dabbawalaSimulatorService;
    @Autowired
    private MailSenderSimulator mailSenderSimulator;

    @Test
    void uc30_importValidRechnungFromExternal_isFoundInInternalSystem() {
        // see https://confluence.bedag.ch/pages/viewpage.action?pageId=204210378
        // "Das System erhält einen neuen Rechnungseingang"

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun sollte die rechnung im internen zugang (dabbawala vorhanden sein).
        var rechnung = internalRechnnungService.getRechnungById(id);
        Assertions.assertEquals(rechnung.iban(), enteredRechnung.rechnung().iban());

        // auch schauen, dass das rechnungs-dokument in der dabbawala-dokument-ablage vorhanden ist
        var dabDocument = dabbawala.getDocumentById(rechnung.document());
        Assertions.assertNotNull(dabDocument);
    }

    @Test
    void uc30_importValidRechnungFromExternal_shouldRemoveDocumentExternal() {
        // Steht glaube ich so nicht in der spez, aber das hier überprüft, dass nach einem erfolgreichen import
        // die rechnung (üblicherweise das PDF) im externen zugang gelöscht wird.

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // vor dem import ist das rechnungs-dokument im externen zugang noch vorhanden
        var maybeDocument = enteredRechnung.rechnung().document();
        assert maybeDocument != null;
        Assertions.assertTrue(dbFilesService.fileExists(maybeDocument));

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun sollte die rechnung im internen zugang (dabbawala vorhanden sein).
        var rechnung = internalRechnnungService.getRechnungById(id);
        Assertions.assertEquals(rechnung.iban(), enteredRechnung.rechnung().iban());

        // nun können wir schauen, dass das rechnungs-dokument im externen zugang nicht mehr da ist.
        Assertions.assertFalse(dbFilesService.fileExists(maybeDocument));
    }

    @Test
    void uc33_approveRechnungInDabbawalaWithCorrectPermission_shouldWork() {
        // Hier geben wir (angehlehnt an diesen UC https://confluence.bedag.ch/pages/viewpage.action?pageId=204210384)
        // eine über den externen zugang eingegebene rechnung in dabbawala frei.

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun müssen wir noch die richtigen rechte haben (ja, auch das ist testbar).
        dabbawalaSimulatorService.addRole(KnownDabRoles.APPROVE_RECHNUNG.getRole());

        // nun wird die rechnung freigegeben
        internalRechnnungService.approve(id);
    }

    @Test
    void uc33_approveRechnungInDabbawalaWithMissingPermission_shouldNotWork() {
        // Hier geben wir (angehlehnt an diesen UC https://confluence.bedag.ch/pages/viewpage.action?pageId=204210384)
        // eine über den externen zugang eingegebene rechnung in dabbawala frei.

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun schauen wir, dass wir die korrekte rolle NICHT haben
        dabbawalaSimulatorService.removeRole(KnownDabRoles.APPROVE_RECHNUNG.getRole());

        // nun wird die rechnung freigegeben: Das wird nicht funktionieren, da wir die rolle nicht haben
        ValidationExceptionChecker.assertHasExactlyGivenValidationErrors(() -> internalRechnnungService.approve(id),
                InternalRechnnungService.VALIDATION_NO_APPROVE_ROLE);
    }

    @Test
    void uc33_approveAlreadyApprovedRechnungInDabbawala_shouldNotWork() {
        // Hier geben wir (angehlehnt an diesen UC https://confluence.bedag.ch/pages/viewpage.action?pageId=204210384)
        // eine über den externen zugang eingegebene rechnung in dabbawala frei.

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun schauen wir, dass wir die korrekte rolle haben
        dabbawalaSimulatorService.addRole(KnownDabRoles.APPROVE_RECHNUNG.getRole());

        // nun wird die rechnung freigegeben: das wird funktionieren
        internalRechnnungService.approve(id);

        // nun versuchen wir eine bereits freigegebene rechnung noch mal freizugeben: Das wird nicht funktionieren ->
        // darf nicht, da wir hier schon mal was an FIS versenden. Sonst passiert das ggf. doppelt.
        ValidationExceptionChecker.assertHasExactlyGivenValidationErrors(() -> internalRechnnungService.approve(id),
                InternalRechnnungService.VALIDATION_INVALID_STATE_MUTATION);
    }

    @Test
    void uc33_approveRechnungInDabbawala_shouldSendMail() {
        // Steht zwar so nicht in der Spez, aber so einen ähnlichen fall wirds geben: Ne mail wird verschickt, falls
        // irgend was passiert (wie in diesem fall eine Freigabe).

        // Rechnung wird im Öffentlichkeitsmodul eingegeben
        var enteredRechnung = enterExternalRechnung();
        var id = enteredRechnung.id();

        // nun wird die datensychronisierung getriggered: Die daten werden vom externen zugang
        // in den internen zugang geladen.
        importerService.triggerImport();

        // nun schauen wir, dass wir die korrekte rolle haben
        dabbawalaSimulatorService.addRole(KnownDabRoles.APPROVE_RECHNUNG.getRole());
        // nun wird die rechnung freigegeben: das wird funktionieren
        internalRechnnungService.approve(id);

        // nun schauen wir, dass ne mail gesendet wurde: Ich gehe mal davon aus, dass die dossier-nummer irgendwo im
        // body der mail auftauchen sollte.
        var dossierNumber = enteredRechnung.rechnung().dossierNumber();
        assert dossierNumber != null;
        var maybeFoundMail = mailSenderSimulator.findMail((mail) -> mail.body().contains(dossierNumber.value()));
        Assertions.assertTrue(maybeFoundMail.isPresent());
    }

    private ExternallyEnteredRechnung enterExternalRechnung() {
        // Rechnung im externen Zugang ("Öffentlichkeitszugang") eingeben
        var dossierNumber = new DossierNumber("e2022.kbl.22");
        var iban = new Iban("CH7985545454564564");
        var number = new RechnungReferenceNumber("654654654564564654687787");
        var document = dbFilesTestService.saveAnyFile();
        var enteredRechnung = new EnteredRechnung(dossierNumber, iban, number, document);
        var id = externalRechnungService.saveUserEnteredRechnung(enteredRechnung);
        return new ExternallyEnteredRechnung(id, enteredRechnung);
    }

    private record ExternallyEnteredRechnung(RechnungId id, @NotNull EnteredRechnung rechnung) {
    }
}
