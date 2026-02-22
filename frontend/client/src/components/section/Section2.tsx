'use client';

import { useEffect, useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay } from 'swiper/modules';
import { ProductItem } from '@/components/product/ProductItem';
import 'swiper/css';

export const Section2 = () => {
  const [timeLeft, setTimeLeft] = useState({
    days: 0,
    hours: 0,
    minutes: 0,
    seconds: 0,
  });

  useEffect(() => {
    const targetDate = new Date('2025-05-18T20:00:00').getTime();

    const interval = setInterval(() => {
      const now = new Date().getTime();
      const distance = targetDate - now;

      if (distance < 0) {
        clearInterval(interval);
        return;
      }

      setTimeLeft({
        days: Math.floor(distance / (1000 * 60 * 60 * 24)),
        hours: Math.floor(
          (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        ),
        minutes: Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60)),
        seconds: Math.floor((distance % (1000 * 60)) / 1000),
      });
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <>
      {/* Section 2 */}
      <div
        className="section-2 py-[45px] sm:py-[60px]"
        data-aos="fade-up"
        data-aos-duration="800"
        data-aos-delay="600"
      >
        <div className="container">
          <div className="inner-wrap bg-[var(--color-primary)] shadow-[0px_4px_4px_0px_#00000040] rounded-[10px] p-[30px_15px] sm:p-[32px_27px_32px_23px] flex flex-wrap md:flex-nowrap gap-[32px]">
            <div className="inner-info w-full md:w-[calc(50%-32px)] lg:w-[calc(32%-32px)] xl:w-[calc(25%-32px)] text-center">
              <h2 className="inner-title mt-0 sm:mt-[24px] mb-[10px] font-bold text-[28px] leading-[150%] text-white">
                ƯU ĐÃI 2024
                <br />
                TOUR GIỜ CHÓT
              </h2>
              <div className="inner-desc font-normal text-[12px] text-white mb-[36px]">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
                eiusmod tempor
              </div>
              <div className="inner-clock-title font-bold text-[21px] text-white mb-[16px]">
                Kết thúc sau
              </div>
              <div className="inner-clock-number flex justify-center gap-[17px] mb-[47px]">
                <div className="inner-item w-[57px] h-[57px] rounded-[10px] bg-white inline-flex justify-center items-center flex-col font-bold text-[var(--color-primary)]">
                  <div className="inner-number text-[24px]">
                    {String(timeLeft.days).padStart(2, '0')}
                  </div>
                  <div className="inner-label text-[12px]">Ngày</div>
                </div>
                <div className="inner-item w-[57px] h-[57px] rounded-[10px] bg-white inline-flex justify-center items-center flex-col font-bold text-[var(--color-primary)]">
                  <div className="inner-number text-[24px]">
                    {String(timeLeft.hours).padStart(2, '0')}
                  </div>
                  <div className="inner-label text-[12px]">Giờ</div>
                </div>
                <div className="inner-item w-[57px] h-[57px] rounded-[10px] bg-white inline-flex justify-center items-center flex-col font-bold text-[var(--color-primary)]">
                  <div className="inner-number text-[24px]">
                    {String(timeLeft.minutes).padStart(2, '0')}
                  </div>
                  <div className="inner-label text-[12px]">Phút</div>
                </div>
                <div className="inner-item w-[57px] h-[57px] rounded-[10px] bg-white inline-flex justify-center items-center flex-col font-bold text-[var(--color-primary)]">
                  <div className="inner-number text-[24px]">
                    {String(timeLeft.seconds).padStart(2, '0')}
                  </div>
                  <div className="inner-label text-[12px]">Giây</div>
                </div>
              </div>
              <div className="inner-discount-title font-bold text-[16px] text-white">
                GIẢM ĐẾN
              </div>
              <div className="inner-discount-price font-bold text-[40px] text-[#FFD63A]">
                990.000<span className="inner-unit underline">đ</span>
              </div>
            </div>
            <div className="inner-list w-full md:w-[50%] lg:w-[68%] xl:w-[75%] overflow-hidden">
              <Swiper
                modules={[Autoplay]}
                spaceBetween={20}
                slidesPerView={1}
                autoplay={{
                  delay: 3000,
                  disableOnInteraction: false,
                }}
                breakpoints={{
                  576: {
                    slidesPerView: 2,
                  },
                  1200: {
                    slidesPerView: 3,
                  },
                }}
                className="swiper-section-2"
              >
                <SwiperSlide>
                  <ProductItem />
                </SwiperSlide>
                <SwiperSlide>
                  <ProductItem />
                </SwiperSlide>
                <SwiperSlide>
                  <ProductItem />
                </SwiperSlide>
                <SwiperSlide>
                  <ProductItem />
                </SwiperSlide>
                <SwiperSlide>
                  <ProductItem />
                </SwiperSlide>
              </Swiper>
            </div>
          </div>
        </div>
      </div>
      {/* End Section 2 */}
    </>
  );
};
