package ch.bedag.sie.dij.optimo.text;

public interface TranslationService {
    TranslatedText text(I18nKey key);

    TranslatedText text(String key);

    TranslatedTextWithKey textWithKey(String key);

    TranslatedTextWithKey textWithKey(I18nKey key);
}
