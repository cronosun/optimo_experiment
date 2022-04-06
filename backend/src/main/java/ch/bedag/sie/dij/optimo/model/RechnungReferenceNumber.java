package ch.bedag.sie.dij.optimo.model;

import org.springframework.lang.Nullable;

public record RechnungReferenceNumber(String value) {
    public RechnungReferenceNumber {
        assert value != null;
    }

    @Nullable
    public static RechnungReferenceNumber fromNullable(@Nullable String value) {
        if (value == null) {
            return null;
        }
        return new RechnungReferenceNumber(value);
    }
}
