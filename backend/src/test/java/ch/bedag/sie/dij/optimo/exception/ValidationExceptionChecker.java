package ch.bedag.sie.dij.optimo.exception;

import ch.bedag.sie.dij.optimo.text.I18nKey;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ValidationExceptionChecker {
    public static void assertHasExactlyGivenValidationErrors(Runnable runnable, I18nKey... errors) {
        try {
            runnable.run();
            throw new RuntimeException("Did not throw any validation errors, expected: " + Arrays.toString(errors));
        } catch (ValidationException exception) {
            var messages = exception.getMessages();
            var expectedErrorsSet = Arrays.stream(errors).collect(Collectors.toSet());

            for (var message : messages) {
                if (!expectedErrorsSet.contains(message.key())) {
                    throw new AssertionError("Got validation error that should not be there: " + message.key());
                }
            }
            if (expectedErrorsSet.size() != messages.size()) {
                throw new AssertionError("Got more or fewer validation errors. Expected: " + expectedErrorsSet + "; got: " + messages);
            }
        }
    }
}
