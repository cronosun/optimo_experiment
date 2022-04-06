package ch.bedag.sie.dij.optimo.mail.simulator;

import ch.bedag.sie.dij.optimo.mail.MailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class MailSenderSimulator implements MailSender {

    private final List<SentMail> sentMails = new ArrayList<>();

    @Override
    public void sendMail(String to, String from, String subject, String body) {
        sentMails.add(new SentMail(to, from, subject, body));
    }

    public Optional<SentMail> findMail(Predicate<SentMail> predicate) {
        return sentMails.stream().filter(predicate).findFirst();
    }
}
