package ch.bedag.sie.dij.optimo.exception;

import ch.bedag.sie.dij.optimo.text.TranslatedTextWithKey;

import java.util.ArrayList;
import java.util.List;

public final class ValidationExceptionCollector {
    private List<TranslatedTextWithKey> messages;

    public void add(TranslatedTextWithKey message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    public void throwIfAnyValidationErrors() {
        var messages = this.messages;
        if (messages != null && !messages.isEmpty()) {
            throw new ValidationException(messages);
        }
    }
}
