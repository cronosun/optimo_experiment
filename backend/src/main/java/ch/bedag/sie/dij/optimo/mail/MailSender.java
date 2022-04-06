package ch.bedag.sie.dij.optimo.mail;

public interface MailSender {
    /**
     * Inserts the mail into the mail outbox (database). The system will then try to send that mail (will remove
     * the mail from the database on success).
     */
    void sendMail(String to, String from, String subject, String body);
}
