package service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {
	private static final String FROM_EMAIL = "thanhdatfptshop123@gmail.com";   // Gmail bạn
    private static final String APP_PASSWORD = "ordj auoq orpe rcsx"; // App password 16 chữ

    public static void sendEmail(String toEmail, String subject, String content) {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");      // TLS port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");// Bật TLS

        // Tạo session xác thực Gmail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("Email đã gửi thành công đến: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Gửi email thất bại!");
        }
    }
}
