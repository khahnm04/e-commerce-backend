'use client';

import { BoxTitle } from '@/components/common/BoxTitle';
import Link from 'next/link';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Pagination } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';

/* ------------------------------------------------------------------ */
/*  Types & Constants                                                 */
/* ------------------------------------------------------------------ */

interface BannerItem {
  image: string;
  alt: string;
  href: string;
}

const BANNERS: BannerItem[] = [
  { image: '/assets/images/banner-1.png', alt: 'Khuyến mại 1', href: '#' },
  { image: '/assets/images/banner-2.png', alt: 'Khuyến mại 2', href: '#' },
  { image: '/assets/images/banner-3.png', alt: 'Khuyến mại 3', href: '#' },
  { image: '/assets/images/banner-2.png', alt: 'Khuyến mại 4', href: '#' },
  { image: '/assets/images/banner-3.png', alt: 'Khuyến mại 5', href: '#' },
];

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const PromotionBanner = () => {
  return (
    <section className="promotion-banner pb-[45px] sm:pb-[60px]">
      <div className="container">
        <BoxTitle
          dataAos="fade-up"
          dataAosDuration="800"
          dataAosDelay="200"
        >
          Khuyến Mại Bùng Nổ - Đánh Tan Nóng Bức
        </BoxTitle>

        <div
          data-aos="fade-up"
          data-aos-duration="800"
          data-aos-delay="400"
        >
          <Swiper
            modules={[Pagination]}
            pagination={{ clickable: true }}
            spaceBetween={20}
            slidesPerView={1}
            breakpoints={{
              576: { slidesPerView: 2 },
              992: { slidesPerView: 3 },
            }}
            className="swiper-promotion-banner"
          >
            {BANNERS.map(({ image, alt, href }, index) => (
              <SwiperSlide key={index}>
                <div className="aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href={href}>
                    <img
                      src={image}
                      alt={alt}
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
            ))}
          </Swiper>
        </div>
      </div>
    </section>
  );
};
