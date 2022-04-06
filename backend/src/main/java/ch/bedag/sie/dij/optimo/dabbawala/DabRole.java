package ch.bedag.sie.dij.optimo.dabbawala;

public record DabRole(String value) {
    public DabRole {
        assert value != null;
    }
}
