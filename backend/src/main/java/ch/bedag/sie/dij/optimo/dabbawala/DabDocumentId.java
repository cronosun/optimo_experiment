package ch.bedag.sie.dij.optimo.dabbawala;

import java.util.UUID;

public record DabDocumentId(UUID value) {
    public DabDocumentId {
        assert value != null;
    }
}
