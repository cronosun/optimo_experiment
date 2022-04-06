package ch.bedag.sie.dij.optimo.text;

public record I18nKey(String key) {
    public static I18nKey of(String key) {
        return new I18nKey(key);
    }
}
