package ch.bedag.sie.dij.optimo.exception;

import ch.bedag.sie.dij.optimo.text.TranslatedTextWithKey;

import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<TranslatedTextWithKey> messages;

    public ValidationException(List<TranslatedTextWithKey> messages) {
        this.messages = Collections.unmodifiableList(messages);
    }

    public static ValidationException of(TranslatedTextWithKey message) {
        return new ValidationException(List.of(message));
    }

    public List<TranslatedTextWithKey> getMessages() {
        return messages;
    }
}
