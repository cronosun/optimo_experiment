package ch.bedag.sie.dij.optimo.model;

import org.springframework.lang.Nullable;

public record KesbStandortId(String value) {

    public KesbStandortId {
        assert value != null;
    }

    @Nullable
    public static KesbStandortId fromNullable(@Nullable String value) {
        if (value == null) {
            return null;
        }
        return new KesbStandortId(value);
    }
}
