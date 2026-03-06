package com.khahnm04.ecommerce.modules.news.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsProductResponse {

    private Long productId;
    private String productName;
    private String productSlug;
    private String productThumbnail;
    private BigDecimal productPrice;

}
