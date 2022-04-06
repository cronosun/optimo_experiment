package ch.bedag.sie.dij.optimo.dbfiles;

import ch.bedag.sie.dij.optimo.persistence.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

// INFO: Entities können nun package private sein
@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "file")
class FileEntity extends BaseEntity {
    @Nullable
    @Column(name = "mime_type", length = 512)
    private String mimeType;

    @NotNull
    @Column(name = "filename", length = 4096)
    private String filename;

    @NotNull
    @Lob
    @Column(name = "content", length = 1024 * 1024 * 96)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;
}
