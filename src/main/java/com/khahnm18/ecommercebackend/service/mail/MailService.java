package com.khahnm18.ecommercebackend.service.mail;

import java.util.Map;

public interface MailService {

    void sendEmail(String to, String subject, String templateName, Map<String, Object> params);

}
