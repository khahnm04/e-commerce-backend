'use client';
import { useEffect, useState } from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Autoplay } from 'swiper/modules';
import { ProductItem } from '@/components/product/ProductItem';
import 'swiper/css';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface TimeLeft {
  days: number;
  hours: number;
  minutes: number;
  seconds: number;
}

interface CountdownUnit {
  key: keyof TimeLeft;
  label: string;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const TARGET_DATE = new Date('2025-05-18T20:00:00').getTime();

const COUNTDOWN_UNITS: CountdownUnit[] = [
  { key: 'days', label: 'Ngày' },
  { key: 'hours', label: 'Giờ' },
  { key: 'minutes', label: 'Phút' },
  { key: 'seconds', label: 'Giây' },
];

const PRODUCT_SLIDE_COUNT = 5;

/* ------------------------------------------------------------------ */
/*  Hooks                                                             */
/* ------------------------------------------------------------------ */

const useCountdown = (targetDate: number): TimeLeft => {
  const [timeLeft, setTimeLeft] = useState<TimeLeft>({
    days: 0,
    hours: 0,
    minutes: 0,
    seconds: 0,
  });

  useEffect(() => {
    const interval = setInterval(() => {
      const distance = targetDate - Date.now();

      if (distance < 0) {
        clearInterval(interval);
        return;
      }

      setTimeLeft({
        days: Math.floor(distance / (1000 * 60 * 60 * 24)),
        hours: Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
        minutes: Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60)),
        seconds: Math.floor((distance % (1000 * 60)) / 1000),
      });
    }, 1000);

    return () => clearInterval(interval);
  }, [targetDate]);

  return timeLeft;
};

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const CountdownTimer = ({
  timeLeft,
  units,
}: {
  timeLeft: TimeLeft;
  units: CountdownUnit[];
}) => (
  <div className="flex justify-center gap-[17px] mb-[47px]">
    {units.map(({ key, label }) => (
      <div
        key={key}
        className="w-[57px] h-[57px] rounded-[10px] bg-white inline-flex justify-center items-center flex-col font-bold text-[var(--color-primary)]"
      >
        <span className="text-[24px]">
          {String(timeLeft[key]).padStart(2, '0')}
        </span>
        <span className="text-[12px]">{label}</span>
      </div>
    ))}
  </div>
);

const SaleInfo = () => (
  <div className="w-full md:w-[calc(50%-32px)] lg:w-[calc(32%-32px)] xl:w-[calc(25%-32px)] text-center">
    <h2 className="mt-0 sm:mt-[24px] mb-[10px] font-bold text-[28px] leading-[150%] text-white">
      ƯU ĐÃI 2024
      <br />
      TOUR GIỜ CHÓT
    </h2>
    <p className="m-0 font-normal text-[12px] text-white mb-[36px]">
      Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
    </p>
    <p className="m-0 font-bold text-[21px] text-white mb-[16px]">Kết thúc sau</p>
  </div>
);

const DiscountBadge = ({
  price,
  currency,
}: {
  price: string;
  currency: string;
}) => (
  <>
    <p className="m-0 font-bold text-[16px] text-white">GIẢM ĐẾN</p>
    <p className="m-0 font-bold text-[40px] text-[#FFD63A]">
      {price}
      <span className="underline">{currency}</span>
    </p>
  </>
);

const ProductCarousel = ({ count }: { count: number }) => (
  <div className="w-full md:w-[50%] lg:w-[68%] xl:w-[75%] overflow-hidden">
    <Swiper
      modules={[Autoplay]}
      spaceBetween={20}
      slidesPerView={1}
      autoplay={{ delay: 3000, disableOnInteraction: false }}
      breakpoints={{
        576: { slidesPerView: 2 },
        1200: { slidesPerView: 3 },
      }}
      className="swiper-flash-sale"
    >
      {Array.from({ length: count }, (_, index) => (
        <SwiperSlide key={index}>
          <ProductItem />
        </SwiperSlide>
      ))}
    </Swiper>
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const FlashSaleCountdown = () => {
  const timeLeft = useCountdown(TARGET_DATE);

  return (
    <section
      className="flash-sale py-[45px] sm:py-[60px]"
      data-aos="fade-up"
      data-aos-duration="800"
      data-aos-delay="600"
    >
      <div className="container">
        <div className="bg-[var(--color-primary)] shadow-[0px_4px_4px_0px_#00000040] rounded-[10px] p-[30px_15px] sm:p-[32px_27px_32px_23px] flex flex-wrap md:flex-nowrap gap-[32px]">
          {/* Left — Sale info + countdown */}
          <div className="w-full md:w-[calc(50%-32px)] lg:w-[calc(32%-32px)] xl:w-[calc(25%-32px)] text-center">
            <h2 className="mt-0 sm:mt-[24px] mb-[10px] font-bold text-[28px] leading-[150%] text-white">
              ƯU ĐÃI 2024
              <br />
              TOUR GIỜ CHÓT
            </h2>
            <p className="m-0 font-normal text-[12px] text-white mb-[36px]">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
            </p>
            <p className="m-0 font-bold text-[21px] text-white mb-[16px]">Kết thúc sau</p>

            <CountdownTimer timeLeft={timeLeft} units={COUNTDOWN_UNITS} />
            <DiscountBadge price="990.000" currency="đ" />
          </div>

          {/* Right — Product carousel */}
          <ProductCarousel count={PRODUCT_SLIDE_COUNT} />
        </div>
      </div>
    </section>
  );
};
