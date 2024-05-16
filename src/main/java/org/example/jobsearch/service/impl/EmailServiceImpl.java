package org.example.jobsearch.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;

    @Override
    public void sendEmail(String toEmail, String link, String nameOfUser) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(EMAIL_FROM, "Job Search Support");
        mimeMessageHelper.setTo(toEmail);
        String subject = "Восстановление пароля";
        String content = "<html><body>";
        content += "<h2 style='color: #007bff;'>Восстановление пароля</h2>";
        content += "<p>Здравствуйте, " + nameOfUser + "</p>";
        content += "<p>На сайте JobSearch был сделан запрос создать новый пароль к вашему аккаунту, связанному с адресом " + toEmail + ".</p>";
        content += "<p>Проследуйте по этой ссылке, чтобы задать пароль: <a href='" + link + "'>Нажмите сюда</a></p>";
        content += "<p>Если эта ссылка не работает, откройте новое окно браузера, а затем скопируйте и вставьте URL-адрес в адресную строку.</p>";
        content += "<p>Если вы не делали такого запроса, проигнорируйте это письмо, и пароль останется прежним.</p>";
        content += "<p>С уважением,</p>";
        content += "<p>Команда JobSearch</p>";
        content += "</body></html>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }

}
