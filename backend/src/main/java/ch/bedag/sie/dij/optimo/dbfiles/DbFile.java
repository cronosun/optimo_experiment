package ch.bedag.sie.dij.optimo.dbfiles;

import ch.bedag.sie.dij.optimo.files.File;
import org.springframework.lang.Nullable;

public record DbFile(
        DbFileId id,
        @Nullable
        String mimeType,
        String filename,
        byte[] content
) implements File {
}
