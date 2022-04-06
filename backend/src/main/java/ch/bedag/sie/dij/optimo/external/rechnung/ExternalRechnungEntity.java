package ch.bedag.sie.dij.optimo.external.rechnung;

import ch.bedag.sie.dij.optimo.model.DossierNumberValidity;
import ch.bedag.sie.dij.optimo.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

// INFO: Entities können nun package private sein
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "external_rechnung")
class ExternalRechnungEntity extends BaseEntity {

    @NotNull
    @Column(name = "dossier_number", length = 512)
    private String dossierNumber;

    @NotNull
    @Column(name = "dossier_number_validity", length = 4096)
    private DossierNumberValidity dossierNumberValidity;

    @Nullable
    @Column(name = "kesb_standort_id", length = 4096)
    private String kesbStandortId;

    @Nullable
    @Column(name = "sozialdienst_id", length = 4096)
    private String sozialdienstId;

    @Nullable
    @Column(name = "iban", length = 4096)
    private String iban;

    @Nullable
    @Column(name = "reference_number", length = 4096)
    private String referenceNumber;

    @NotNull
    @Column(name = "rechnung_file_id", length = 4096)
    private UUID rechnungFileId;
}
