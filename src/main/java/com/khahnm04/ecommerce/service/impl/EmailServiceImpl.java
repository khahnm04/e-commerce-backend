package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.dto.request.email.EmailRequest;
import com.khahnm04.ecommerce.dto.request.email.Recipient;
import com.khahnm04.ecommerce.dto.request.email.Sender;
import com.khahnm04.ecommerce.repository.httpclient.EmailClient;
import com.khahnm04.ecommerce.service.EmailService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailClient emailClient;
    private final TemplateEngine templateEngine;

    @Value("${notification.email.brevo-apikey}")
    private String apiKey;

    @Value("${notification.email.sender-name}")
    private String senderName;

    @Value("${notification.email.sender-email}")
    private String senderEmail;

    @Value("${notification.email.otp-subject}")
    private String otpSubject;

    @Async
    @Override
    public void sendOtpEmail(String to, String otp) {
        try {
            Context context = new Context();
            context.setVariable("otp", otp);
            String htmlContent = templateEngine.process("email/otp-email", context);

            EmailRequest request = EmailRequest.builder()
                    .sender(Sender.builder()
                            .name(senderName)
                            .email(senderEmail)
                            .build())
                    .to(List.of(Recipient.builder()
                            .email(to)
                            .build()))
                    .subject(otpSubject)
                    .htmlContent(htmlContent)
                    .build();

            var response = emailClient.sendEmail(apiKey, request);
            log.info("OTP email sent successfully to {} - messageId: {}", to, response.messageId());
        } catch (FeignException e) {
            log.error("Failed to send OTP email to {}: status={}, body={}", to, e.status(), e.contentUTF8(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending OTP email to {}: {}", to, e.getMessage(), e);
        }
    }

}
