package com.khahnm18.ecommercebackend.dto.response.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenerateSlugResponse(

    String slug

) {}
