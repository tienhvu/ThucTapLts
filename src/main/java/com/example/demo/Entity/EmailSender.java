package com.example.demo.Entity;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String EMAIL = "vutien827@gmail.com";
    private static final String PASSWORD = "ozzolmookyjjwuwc";

    public static void sendEmail(String recipientEmail, String subject, String content) {
        // Cấu hình thông tin đăng nhập email
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Tạo session đăng nhập email
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            // Tạo đối tượng Message
            Message message = new MimeMessage(session);

            // Đặt thông tin người gửi
            message.setFrom(new InternetAddress(EMAIL));

            // Đặt thông tin người nhận
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Đặt tiêu đề email
            message.setSubject(subject);

            // Đặt nội dung email
            message.setText(content);

            // Gửi email
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}