import { BoxTitle } from '@/components/common/BoxTitle';
import { ButtonOutline } from '@/components/common/ButtonOutline';
import { ProductItem } from '@/components/product/ProductItem';

const PRODUCT_COUNT = 8;

export const FeaturedProducts = () => {
  return (
    <section className="featured-products pb-[45px] sm:pb-[60px]">
      <div className="container">
        <BoxTitle
          dataAos="fade-up"
          dataAosDuration="800"
          dataAosDelay="200"
        >
          Tour Trong Nước
        </BoxTitle>

        <div
          className="grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[10px] sm:gap-[20px]"
          data-aos="fade-up"
          data-aos-duration="800"
          data-aos-delay="400"
        >
          {Array.from({ length: PRODUCT_COUNT }, (_, index) => (
            <ProductItem key={index} />
          ))}
        </div>

        <div className="mt-[25px] sm:mt-[30px] text-center">
          <ButtonOutline>Xem tất cả</ButtonOutline>
        </div>
      </div>
    </section>
  );
};
