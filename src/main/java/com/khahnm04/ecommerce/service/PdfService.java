package com.khahnm04.ecommerce.service;

import java.util.Map;

public interface PdfService {

    byte[] generatePdfFromTemplate(String templateName, Map<String, Object> data);

}
