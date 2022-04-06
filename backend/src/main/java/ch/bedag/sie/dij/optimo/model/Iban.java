package ch.bedag.sie.dij.optimo.model;

import org.springframework.lang.Nullable;

public record Iban(String value) {
    public Iban {
        assert value != null;
    }

    @Nullable
    public static Iban fromNullable(@Nullable String value) {
        if (value == null) {
            return null;
        }
        return new Iban(value);
    }
}
