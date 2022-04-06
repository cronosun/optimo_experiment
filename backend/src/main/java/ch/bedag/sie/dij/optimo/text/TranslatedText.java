package ch.bedag.sie.dij.optimo.text;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public final class TranslatedText {
    private final Map<Language, String> texts;

    public TranslatedText(Map<Language, String> texts) {
        this.texts = Collections.unmodifiableMap(texts);
    }

    public Optional<String> getText(Language language) {
        return Optional.ofNullable(this.texts.get(language));
    }
}
