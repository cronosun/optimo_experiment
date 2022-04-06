package ch.bedag.sie.dij.optimo.model;

import org.springframework.lang.Nullable;

public record SozialdienstId(String value) {
    public SozialdienstId {
        assert value != null;
    }

    @Nullable
    public static SozialdienstId fromNullable(@Nullable String value) {
        if (value == null) {
            return null;
        }
        return new SozialdienstId(value);
    }
}
