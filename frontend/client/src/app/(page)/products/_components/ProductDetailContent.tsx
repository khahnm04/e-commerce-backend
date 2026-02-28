import { BoxImages } from '@/components/common/BoxImages';
import { BoxTourInfo } from '@/components/common/BoxTourInfo';
import { BoxTourSchedule } from '@/components/common/BoxTourSchedule';
import { BoxTourDetail } from '@/components/common/BoxTourDetail';

export const ProductDetailContent = () => {
  return (
    <section className="product-detail py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        <div className="flex flex-wrap lg:flex-nowrap">
          {/* Main content — images, info, schedule */}
          <div className="w-full lg:w-[58.7%] lg:mr-[20px] order-1 lg:order-none mt-[30px] lg:mt-0">
            <BoxImages />
            <BoxTourInfo />
            <BoxTourSchedule />
          </div>

          {/* Sidebar — booking detail */}
          <aside className="flex-1 w-full lg:w-auto">
            <BoxTourDetail />
          </aside>
        </div>
      </div>
    </section>
  );
};
