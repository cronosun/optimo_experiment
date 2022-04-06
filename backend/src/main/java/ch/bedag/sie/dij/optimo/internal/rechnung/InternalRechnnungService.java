package ch.bedag.sie.dij.optimo.internal.rechnung;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocumentId;
import ch.bedag.sie.dij.optimo.dabbawala.Dabbawala;
import ch.bedag.sie.dij.optimo.exception.ValidationException;
import ch.bedag.sie.dij.optimo.mail.MailSender;
import ch.bedag.sie.dij.optimo.model.DossierNumber;
import ch.bedag.sie.dij.optimo.model.Iban;
import ch.bedag.sie.dij.optimo.model.KesbStandortId;
import ch.bedag.sie.dij.optimo.model.KnownDabRoles;
import ch.bedag.sie.dij.optimo.model.RechnungId;
import ch.bedag.sie.dij.optimo.model.RechnungReferenceNumber;
import ch.bedag.sie.dij.optimo.model.RechnungStatus;
import ch.bedag.sie.dij.optimo.model.SozialdienstId;
import ch.bedag.sie.dij.optimo.text.I18nKey;
import ch.bedag.sie.dij.optimo.text.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class InternalRechnnungService {

    public static final I18nKey VALIDATION_NO_APPROVE_ROLE = I18nKey.of("internal.rechnung.no_rechnung_approve_role");
    public static final I18nKey VALIDATION_NOT_FOUND = I18nKey.of("internal.rechnung.not_found");
    public static final I18nKey VALIDATION_INVALID_STATE_MUTATION = I18nKey.of("internal.rechnung.invalid_state_mutation");

    private final InternalRechnungRepository internalRechnungRepository;
    private final Dabbawala dabbawala;
    private final TranslationService translationService;
    private final MailSender mailSender;

    public RechnungId saveOrUpdateRechnung(InternalRechnung rechnung) {
        var entity = map(rechnung);
        internalRechnungRepository.save(entity);
        return new RechnungId(entity.getId());
    }

    public InternalRechnung getRechnungById(RechnungId id) {
        var entity = internalRechnungRepository.getById(id.value());
        return map(entity);
    }

    public InternalRechnung maybeGetRechnungById(RechnungId id) {
        var entities = internalRechnungRepository.findAllById(Collections.singleton(id.value()));
        var first = entities.stream().findFirst();
        return first.map(this::map).orElse(null);
    }

    /**
     * Rechnung freigeben: https://confluence.bedag.ch/pages/viewpage.action?pageId=204210384
     */
    public void approve(RechnungId id) {
        // Spezifikation: Der Benutzer hat die Rolle APPROVE_RECHNUNG?
        var roles = dabbawala.getRoles();
        if (!roles.contains(KnownDabRoles.APPROVE_RECHNUNG.getRole())) {
            throw ValidationException.of(translationService.textWithKey(VALIDATION_NO_APPROVE_ROLE));
        }

        // rechnung laden.
        var rechnung = maybeGetRechnungById(id);
        if (rechnung == null) {
            throw ValidationException.of(translationService.textWithKey(VALIDATION_NOT_FOUND));
        }

        // status auf approve setzen: Das geht nur, wenn der bisherige status sich im zustand "NEW" befindet. Eine
        // bereits approved rechnung kann nicht noch mal approved werden. Eine abgelehnte ebenfalls nicht.
        if (rechnung.status() != RechnungStatus.NEW) {
            throw ValidationException.of(translationService.textWithKey(VALIDATION_INVALID_STATE_MUTATION));
        }
        var changedRechnung = rechnung.withStatus(RechnungStatus.APPROVED);
        saveOrUpdateRechnung(changedRechnung);
        // und zuguterletzt noch ein bestätigunsmail senden.
        sendApproveMail(changedRechnung);
    }

    private void sendApproveMail(InternalRechnung rechnung) {
        var dossierId = rechnung.dossierNumber().value();
        mailSender.sendMail("some.recipient@bedag.ch", "do-not-reply@dji.bedag.ch", "Rechnung genehmigt",
                "Die Rechnung für das Dossier " + dossierId + " wurde genehmigt.");
    }

    private InternalRechnungEntity map(InternalRechnung rechnung) {
        var entity = new InternalRechnungEntity();
        entity.setDossierNumber(rechnung.dossierNumber().value());
        entity.setId(rechnung.id().value());
        entity.setDossierNumberValidity(rechnung.dossierNumberValidity());
        if (rechnung.iban() != null) {
            entity.setIban(rechnung.iban().value());
        }
        entity.setRechnungFileId(rechnung.document().value());
        if (rechnung.sozialdienstId() != null) {
            entity.setSozialdienstId(rechnung.sozialdienstId().value());
        }
        entity.setRechnungStatus(rechnung.status());
        if (rechnung.number() != null) {
            entity.setReferenceNumber(rechnung.number().value());
        }
        return entity;
    }

    private InternalRechnung map(InternalRechnungEntity entity) {
        var id = new RechnungId(entity.getId());
        var dossierNumber = new DossierNumber(entity.getDossierNumber());
        var iban = Iban.fromNullable(entity.getIban());
        var number = RechnungReferenceNumber.fromNullable(entity.getReferenceNumber());
        var document = new DabDocumentId(entity.getRechnungFileId());
        var kesbStandortId = KesbStandortId.fromNullable(entity.getKesbStandortId());
        var sozialdienstId = SozialdienstId.fromNullable(entity.getSozialdienstId());
        var dossierNumberValidity = entity.getDossierNumberValidity();
        var status = entity.getRechnungStatus();

        return new InternalRechnung(id, dossierNumber, iban, number, document, kesbStandortId, sozialdienstId, dossierNumberValidity, status);
    }
}
