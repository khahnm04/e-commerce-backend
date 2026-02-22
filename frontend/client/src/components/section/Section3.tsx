'use client';

import { BoxTitle } from '@/components/common/BoxTitle';
import Link from 'next/link';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Pagination } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/pagination';

export const Section3 = () => {
  return (
    <>
      {/* Section 3 */}
      <div className="section-3 pb-[45px] sm:pb-[60px]">
        <div className="container">
          <BoxTitle
            dataAos="fade-up"
            dataAosDuration="800"
            dataAosDelay="200"
          >
            Khuyến Mại Bùng Nổ - Đánh Tan Nóng Bức
          </BoxTitle>
          <div
            className="inner-wrap"
            data-aos="fade-up"
            data-aos-duration="800"
            data-aos-delay="400"
          >
            <Swiper
              modules={[Pagination]}
              pagination={{
                clickable: true,
              }}
              spaceBetween={20}
              slidesPerView={1}
              breakpoints={{
                576: {
                  slidesPerView: 2,
                },
                992: {
                  slidesPerView: 3,
                },
              }}
              className="swiper-section-3"
            >
              <SwiperSlide>
                <div className="inner-item aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href="#">
                    <img
                      src="/assets/images/banner-1.png"
                      alt=""
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="inner-item aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href="#">
                    <img
                      src="/assets/images/banner-2.png"
                      alt=""
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="inner-item aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href="#">
                    <img
                      src="/assets/images/banner-3.png"
                      alt=""
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="inner-item aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href="#">
                    <img
                      src="/assets/images/banner-2.png"
                      alt=""
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
              <SwiperSlide>
                <div className="inner-item aspect-[382/269] rounded-[10px] overflow-hidden">
                  <Link href="#">
                    <img
                      src="/assets/images/banner-3.png"
                      alt=""
                      className="w-full h-full object-cover"
                    />
                  </Link>
                </div>
              </SwiperSlide>
            </Swiper>
          </div>
        </div>
      </div>
      {/* End Section 3 */}
    </>
  );
};
