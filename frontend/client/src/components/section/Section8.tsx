import { BoxImages } from '@/components/common/BoxImages';
import { BoxTourInfo } from '@/components/common/BoxTourInfo';
import { BoxTourSchedule } from '@/components/common/BoxTourSchedule';
import { BoxTourDetail } from '@/components/common/BoxTourDetail';

export const Section8 = () => {
  return (
    <div className="section-8 py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        <div className="inner-wrap flex flex-wrap lg:flex-nowrap">
          <div className="inner-left w-full lg:w-[58.7%] lg:mr-[20px] order-1 lg:order-none mt-[30px] lg:mt-0">
            <BoxImages />
            <BoxTourInfo />
            <BoxTourSchedule />
          </div>
          <div className="inner-right flex-1 w-full lg:w-auto">
            <BoxTourDetail />
          </div>
        </div>
      </div>
    </div>
  );
};
