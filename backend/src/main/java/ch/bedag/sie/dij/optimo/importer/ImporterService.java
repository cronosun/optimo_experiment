package ch.bedag.sie.dij.optimo.importer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * Imports data from "externer zugang" to "interner zugang".
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class ImporterService {

    private final RechnungImportService rechnungImportService;

    public void triggerImport() {
        rechnungImportService.importRechnungen();
    }
}
