package com.khahnm04.ecommerce.modules.news.dto.response;

import com.khahnm04.ecommerce.common.dto.BaseResponse;
import com.khahnm04.ecommerce.modules.news.enums.NewsStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse extends BaseResponse<Long> {

    private String title;
    private String slug;
    private String image;
    private String summary;
    private String content;
    private Long viewCount;
    private Boolean isFeatured;
    private NewsStatus status;
    private LocalDateTime publishedAt;
    private List<NewsProductResponse> products;

}
