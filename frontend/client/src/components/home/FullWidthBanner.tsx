import Link from 'next/link';

export const FullWidthBanner = () => {
  return (
    <section
      className="full-width-banner pb-[45px] sm:pb-[60px]"
      data-aos="fade-up"
      data-aos-duration="800"
      data-aos-delay="200"
    >
      <div className="container">
        <div className="rounded-[10px] overflow-hidden">
          <Link href="#">
            <img
              src="/assets/images/banner-4.png"
              alt="Banner quảng cáo"
              className="w-full h-auto"
            />
          </Link>
        </div>
      </div>
    </section>
  );
};
