package ch.bedag.sie.dij.optimo.model;

import java.util.UUID;

public record RechnungId(UUID value) {
    public RechnungId {
        assert value != null;
    }
}
