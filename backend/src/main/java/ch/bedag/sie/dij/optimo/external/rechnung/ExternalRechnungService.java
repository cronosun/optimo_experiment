package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.cmi.Cmi;
import ch.bedag.sie.dij.optimo.cmi.DossierInfoResult;
import ch.bedag.sie.dij.optimo.dbfiles.DbFileId;
import ch.bedag.sie.dij.optimo.dbfiles.DbFilesService;
import ch.bedag.sie.dij.optimo.exception.ValidationException;
import ch.bedag.sie.dij.optimo.exception.ValidationExceptionCollector;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.DossierNumberValidity;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.KesbStandortId;
import ch.bedag.sie.dij.optimo.model.RechnungId;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import ch.bedag.sie.dij.optimo.model.SozialdienstId;
import ch.bedag.sie.dij.optimo.text.I18nKey;
import ch.bedag.sie.dij.optimo.text.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ExternalRechnungService {

    public static final I18nKey VALIDATION_INVALID_DOSSIER_NUMBER = I18nKey.of("oem.rechnungen.invalid_dossier_number");
    public static final I18nKey VALIDATION_MISSING_DOSSIER_NUMBER = I18nKey.of("oem.rechnungen.missing_dossier_number");
    public static final I18nKey VALIDATION_MISSING_RECHNUNG_DOCUMENT = I18nKey.of("oem.rechnungen.missing_rechnung_document");
    public static final I18nKey VALIDATION_RECHNUNG_DOCUMENT_NOT_PERSISTED = I18nKey.of("oem.rechnungen.rechnung_document_not_persisted");

    private final Cmi cmi;
    private final TranslationService translationService;
    private final DbFilesService filesService;
    private final ExternalRechnungRepository repository;

    /**
     * Method for use case "UC02 Rechnung einliefern" (https://confluence.bedag.ch/display/DIJ/UC02+Rechnung+einliefern)
     */
    public RechnungId saveUserEnteredRechnung(EnteredRechnung rechnung) {
        // validation
        var validationCollector = new ValidationExceptionCollector();
        var dossierNumber = rechnung.dossierNumber();
        if (dossierNumber == null) {
            validationCollector.add(translationService.textWithKey(VALIDATION_MISSING_DOSSIER_NUMBER));
        }
        var rechnungDocument = rechnung.document();
        if (rechnungDocument == null) {
            validationCollector.add(translationService.textWithKey(VALIDATION_MISSING_RECHNUNG_DOCUMENT));
        }
        validationCollector.throwIfAnyValidationErrors();
        assert dossierNumber != null && rechnungDocument != null;

        // make sure the file exists (has been uploaded)
        if (!filesService.fileExists(rechnungDocument)) {
            throw ValidationException.of(translationService.textWithKey(VALIDATION_RECHNUNG_DOCUMENT_NOT_PERSISTED));
        }
        var maybeIban = rechnung.iban();
        var dossierInfoResult = validateDossierNumberAndReturnDossierInfo(rechnung);

        var entity = new ExternalRechnungEntity();
        entity.setDossierNumber(dossierNumber.value());
        if (maybeIban != null) {
            entity.setIban(maybeIban.value());
        }
        entity.setRechnungFileId(rechnungDocument.id());
        // if present, also assign sozialdienst ID & standort ID
        if (dossierInfoResult != null) {
            entity.setSozialdienstId(dossierInfoResult.getSozialdienst().id().value());
            entity.setKesbStandortId(dossierInfoResult.getStandort().id().value());
            entity.setDossierNumberValidity(DossierNumberValidity.VALID);
        } else {
            entity.setDossierNumberValidity(DossierNumberValidity.CHECK_NOT_POSSIBLE_BACKEND_NOT_AVAILABLE);
        }

        repository.save(entity);
        var id = entity.getId();
        return new RechnungId(id);
    }

    public List<ExternalRechnung> listRechnungen(int limit) {
        // TODO: Limit results
        return repository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    public void delete(RechnungId id) {
        repository.deleteById(id.value());
    }

    @Nullable
    private DossierInfoResult.Found validateDossierNumberAndReturnDossierInfo(EnteredRechnung rechnung) {
        var dossierNumberValidity = cmi.getDossierInfo(rechnung.dossierNumber());
        if (dossierNumberValidity instanceof DossierInfoResult.NoSuchNumber) {
            // we cannot continue, the value does not exist
            throw ValidationException.of(translationService.textWithKey(VALIDATION_INVALID_DOSSIER_NUMBER));
        } else if (dossierNumberValidity instanceof DossierInfoResult.BackendNotAvailable) {
            // this is ok, we must also accept rechnungen if backend is not available
            return null;
        } else if (dossierNumberValidity instanceof DossierInfoResult.Found found) {
            return found;
        }
        throw new RuntimeException("unreachable");
    }

    private ExternalRechnung map(ExternalRechnungEntity entity) {
        var id = new RechnungId(entity.getId());
        var dossierNumber = new DossierNumber(entity.getDossierNumber());
        final Iban iban;
        if (entity.getIban() != null) {
            iban = new Iban(entity.getIban());
        } else {
            iban = null;
        }
        final RechnungReferenceNumber rechnungReferenceNumber;
        if (entity.getReferenceNumber() != null) {
            rechnungReferenceNumber = new RechnungReferenceNumber(entity.getReferenceNumber());
        } else {
            rechnungReferenceNumber = null;
        }
        var kesbStandortId = KesbStandortId.fromNullable(entity.getKesbStandortId());
        var sozialdienstId = SozialdienstId.fromNullable(entity.getSozialdienstId());
        var rechnungFile = new DbFileId(entity.getRechnungFileId());
        var dossierNumberValidity = entity.getDossierNumberValidity();

        return new ExternalRechnung(id, dossierNumber, iban, rechnungReferenceNumber, rechnungFile, kesbStandortId, sozialdienstId, dossierNumberValidity);
    }
}
