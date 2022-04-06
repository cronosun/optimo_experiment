package ch.bedag.sie.dij.optimo.files;

import org.springframework.lang.Nullable;

public interface File {
    String filename();

    @Nullable
    String mimeType();

    byte[] content();
}
