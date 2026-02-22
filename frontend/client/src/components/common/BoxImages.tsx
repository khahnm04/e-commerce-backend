"use client"
import { useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { FreeMode, Navigation, Thumbs } from 'swiper/modules';
import { Swiper as SwiperType } from 'swiper';

// Import Swiper styles
import 'swiper/css';
import 'swiper/css/free-mode';
import 'swiper/css/navigation';
import 'swiper/css/thumbs';

interface BoxImagesProps {
  images?: string[];
}

export const BoxImages = ({
  images = [
    '/assets/images/banner-8.jpg',
    '/assets/images/banner-9.jpg',
    '/assets/images/banner-10.jpg',
    '/assets/images/banner-11.jpg',
  ]
}: BoxImagesProps) => {
  const [thumbsSwiper, setThumbsSwiper] = useState<SwiperType | null>(null);

  return (
    <div className="box-images mb-[30px]">
      <div className="inner-images-main mb-[15px] sm:mb-[30px]">
        <Swiper
          spaceBetween={10}
          navigation={true}
          thumbs={{ swiper: thumbsSwiper && !thumbsSwiper.destroyed ? thumbsSwiper : null }}
          modules={[FreeMode, Navigation, Thumbs]}
          className="swiper-box-images-main rounded-[10px] overflow-hidden"
        >
          {images.map((img, index) => (
            <SwiperSlide key={index}>
              <div className="inner-image aspect-[695/480] w-full h-full rounded-[10px] overflow-hidden">
                <img
                  src={img}
                  alt={`Product Image ${index + 1}`}
                  className="w-full h-full object-cover"
                />
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
      <div className="inner-images-thumb">
        <Swiper
          onSwiper={setThumbsSwiper}
          spaceBetween={10}
          slidesPerView={4}
          freeMode={true}
          watchSlidesProgress={true}
          modules={[FreeMode, Navigation, Thumbs]}
          className="swiper-box-images-thumb"
          breakpoints={{
            0: {
              spaceBetween: 5,
            },
            576: {
              spaceBetween: 10,
            }
          }}
        >
          {images.map((img, index) => (
            <SwiperSlide key={index}>
              <div className="inner-image aspect-[166/129] w-full h-full rounded-[10px] overflow-hidden cursor-pointer">
                <img
                  src={img}
                  alt={`Product Thumbnail ${index + 1}`}
                  className="w-full h-full object-cover"
                />
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </div>
  );
};
