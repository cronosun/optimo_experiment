package ch.bedag.sie.dij.optimo.text;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DefaultTranslationService implements TranslationService {
    @Override
    public TranslatedText text(I18nKey key) {
        return textWithKey(key).text();
    }

    @Override
    public TranslatedText text(String key) {
        return textWithKey(key).text();
    }

    @Override
    public TranslatedTextWithKey textWithKey(String key) {
        return textWithKey(new I18nKey(key));
    }

    @Override
    public TranslatedTextWithKey textWithKey(I18nKey key) {
        // TODO: To be implemented (read texts from properties / resources).
        var textMap = new HashMap<Language, String>();
        textMap.put(Language.DE, "Fake Text");
        textMap.put(Language.FR, "FR: Fake Text");
        var text = new TranslatedText(textMap);
        return new TranslatedTextWithKey(key, text);
    }
}
