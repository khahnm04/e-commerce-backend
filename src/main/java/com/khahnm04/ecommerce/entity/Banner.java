package com.khahnm04.ecommerce.entity;

import com.khahnm04.ecommerce.common.enums.BannerPosition;
import com.khahnm04.ecommerce.common.enums.BannerStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "banners")
public class Banner extends BaseEntity<Long> {

  @Column(name = "image", nullable = false, length = 500)
  private String image;

  @Enumerated(EnumType.STRING)
  @Column(name = "position", nullable = false)
  private BannerPosition position;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder;

  @Column(name = "label", length = 100)
  private String label;

  @Column(name = "heading")
  private String heading;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "cta_text", length = 100)
  private String ctaText;

  @Column(name = "cta_url", length = 500)
  private String ctaUrl;

  @Column(name = "countdown_end")
  private LocalDateTime countdownEnd;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private BannerStatus status;

}
