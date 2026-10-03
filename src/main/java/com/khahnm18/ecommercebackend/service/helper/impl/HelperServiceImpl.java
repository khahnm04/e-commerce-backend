package com.khahnm18.ecommercebackend.service.helper.impl;

import com.khahnm18.ecommercebackend.dto.request.helper.GenerateSlugRequest;
import com.khahnm18.ecommercebackend.dto.response.helper.GenerateSlugResponse;
import com.khahnm18.ecommercebackend.service.helper.HelperService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.Locale;

@Slf4j(topic = "HELPER-SERVICE")
@Service
@RequiredArgsConstructor
public class HelperServiceImpl implements HelperService {

    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Pattern NON_ALNUM = Pattern.compile("[^a-z0-9]+");
    private static final Pattern EDGE_DASHES = Pattern.compile("(^-+|-+$)");

    @Override
    public GenerateSlugResponse generateSlug(GenerateSlugRequest request) {
        if (request.input() == null || request.input().isBlank()) {
            return GenerateSlugResponse.builder()
                .slug("")
                .build();
        }

        String s = Normalizer.normalize(request.input().strip(), Normalizer.Form.NFD);
        s = DIACRITICS.matcher(s).replaceAll("");
        s = s.replace('đ', 'd').replace('Đ', 'D');
        s = s.toLowerCase(Locale.ROOT);
        s = NON_ALNUM.matcher(s).replaceAll("-");
        String slugFinal = EDGE_DASHES.matcher(s).replaceAll("");

        return GenerateSlugResponse.builder()
            .slug(slugFinal)
            .build();
    }

}
