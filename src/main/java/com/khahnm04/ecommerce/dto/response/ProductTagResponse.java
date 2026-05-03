package com.khahnm04.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductTagResponse(
    Long productTagId,
    Long tagId,
    String tagName,
    String tagGroupName
) implements Serializable {
}
