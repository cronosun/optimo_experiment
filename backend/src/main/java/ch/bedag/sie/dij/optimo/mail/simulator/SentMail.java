package ch.bedag.sie.dij.optimo.mail.simulator;

public record SentMail(
        String to,
        String from,
        String subject,
        String body) {
}
