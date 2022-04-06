package ch.bedag.sie.dij.optimo.importer;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocumentId;
import ch.bedag.sie.dij.optimo.dabbawala.Dabbawala;
import ch.bedag.sie.dij.optimo.dbfiles.DbFilesService;
import ch.bedag.sie.dij.optimo.external.rechnung.ExternalRechnung;
import ch.bedag.sie.dij.optimo.internal.rechnung.InternalRechnung;
import ch.bedag.sie.dij.optimo.model.RechnungStatus;
import ch.bedag.sie.dij.optimo.internal.rechnung.InternalRechnnungService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RechnungImportService {

    private final DbFilesService dbFilesService;
    private final Dabbawala dabbawala;
    private final InternalRechnnungService internalInternalRechnnungService;
    private final ch.bedag.sie.dij.optimo.external.rechnung.ExternalRechnungService externalRechnungenService;

    public void importRechnungen() {
        // TODO: Nicht 100% implementiert, das abholen muss natürlich über das API passieren, nicht direkt mittels service-zugriff.
        while (true) {
            var listOfRechnungen = externalRechnungenService.listRechnungen(10);
            if (listOfRechnungen.isEmpty()) {
                break;
            }
            for (var rechnung : listOfRechnungen) {
                importSingleRechnung(rechnung);
            }
        }
    }

    private void importSingleRechnung(ExternalRechnung rechnung) {
        // first we have to transfer the rechnung-document.
        var dabDocumentId = importDocumentFromRechnung(rechnung);
        // now create a new internal rechnung
        var status = RechnungStatus.NEW;
        var internalRechnung = new InternalRechnung(rechnung.id(), rechnung.dossierNumber(), rechnung.iban(), rechnung.number(), dabDocumentId, rechnung.kesbStandortId(), rechnung.sozialdienstId(), rechnung.dossierNumberValidity(), status);
        internalInternalRechnnungService.saveOrUpdateRechnung(internalRechnung);
        // rechnung has been imported successfully, can now delete it from "externer zugang"
        externalRechnungenService.delete(rechnung.id());
        // the attached document can also be deleted from the file storage (usually the rechnungs-PDF).
        dbFilesService.delete(rechnung.document());
    }

    private DabDocumentId importDocumentFromRechnung(ExternalRechnung rechnung) {
        var document = rechnung.document();
        // load rechnung from "externer zugang"
        var file = dbFilesService.getFileById(document);
        // save rechnung in "interner zugang" / dabbawala document store
        return dabbawala.saveDocument(file);
    }
}