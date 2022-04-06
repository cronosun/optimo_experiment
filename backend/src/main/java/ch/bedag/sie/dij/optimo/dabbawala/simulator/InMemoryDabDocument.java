package ch.bedag.sie.dij.optimo.dabbawala.simulator;

import ch.bedag.sie.dij.optimo.dabbawala.DabDocument;
import ch.bedag.sie.dij.optimo.files.File;
import org.springframework.lang.Nullable;

record InMemoryDabDocument(String filename,
                           @Nullable
                           String mimeType,
                           byte[] content) implements DabDocument {
    public static InMemoryDabDocument fromFile(File file) {
        return new InMemoryDabDocument(file.filename(), file.mimeType(), file.content());
    }
}
