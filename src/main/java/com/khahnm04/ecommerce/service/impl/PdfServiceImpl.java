package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.service.PdfService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServiceImpl implements PdfService {

    private final SpringTemplateEngine templateEngine;

    @Override
    public byte[] generatePdfFromTemplate(String templateName, Map<String, Object> data) {
        try {
            Context context = new Context();
            context.setVariables(data);
            String html = templateEngine.process(templateName, context);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);

            ClassPathResource fontResource = new ClassPathResource("fonts/Roboto-Regular.ttf");
            if (fontResource.exists()) {
                builder.useFont(fontResource.getFile(), "Roboto", 400,
                        com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle.NORMAL, true);
            } else {
                log.warn("Font file not found at classpath:fonts/Roboto-Regular.ttf.");
            }

            ClassPathResource mediumFont = new ClassPathResource("fonts/Roboto-Medium.ttf");
            if (mediumFont.exists()) {
                builder.useFont(mediumFont.getFile(), "Roboto", 500,
                        com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle.NORMAL, true);
                builder.useFont(mediumFont.getFile(), "Roboto", 700,
                        com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle.NORMAL, true);
            }

            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

}
