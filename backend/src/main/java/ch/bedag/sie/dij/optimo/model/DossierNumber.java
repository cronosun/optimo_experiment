package ch.bedag.sie.dij.optimo.model;

public record DossierNumber(String value) {
    public DossierNumber {
        assert value != null;
    }
}
