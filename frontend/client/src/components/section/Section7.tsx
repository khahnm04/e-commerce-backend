import { FaEye, FaFilter, FaSquareCaretDown, FaSquareCaretUp, FaTag } from 'react-icons/fa6';
import { BoxFilter } from '@/components/common/BoxFilter';
import { ProductItem } from '@/components/product/ProductItem';
import { BoxPagination } from '@/components/common/BoxPagination';

export const Section7 = () => {
  return (
    <div className="section-7 py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        <div className="inner-wrap flex">
          <div className="inner-left w-[23.5%] mr-[23px] hidden lg:block lg:relative lg:z-[1]">
            <BoxFilter />
          </div>

          {/* Mobile Filter Overlay Logic would need to be added here for "active" state interaction */}
          {/* For now, following exact CSS translation logic */}

          <div className="inner-right flex-1">
            <div className="inner-info mb-[16px]">
              <h2 className="inner-title mt-0 mb-[10px] font-semibold text-[24px] sm:text-[28px] text-[var(--color-primary)]">
                Tour Nước Ngoài
              </h2>
              <div className="inner-desc font-normal text-[14px] text-[#747578] text-justify">
                Du lịch Châu Á: là châu lục lớn và đông dân nhất thế giới. Châu Á cũng có nhiều danh lam thắng cảnh đẹp tại một số quốc gia như Hàn Quốc, Nhật Bản, Trung Quốc, Việt Nam,... và VK Travel là nhà tổ chức du lịch chuyên nghiệp hàng đầu Châu Á, đã 5 lần liên tiếp nhận giải TTG Travel Awards. Hãy cùng 28.Travel du lịch Châu Á để tận hưởng những dịch vụ tốt nhất.
              </div>
            </div>
            <div className="inner-info-2 flex items-center justify-between flex-wrap mb-[36px]">
              <div className="inner-sort flex gap-[20px_16px] sm:gap-[16px] w-full lg:w-auto mb-[25px] lg:mb-0 flex-wrap sm:flex-nowrap">
                <div className="inner-label font-semibold text-[14px] text-[#747578] flex items-center">
                  Sắp xếp :
                </div>
                <button className="inner-button bg-transparent p-0 border-0 cursor-pointer font-normal text-[12px] text-[#747578] flex items-center">
                  Giá tăng dần
                  <FaSquareCaretUp className="text-[16px] ml-[5px]" />
                </button>
                <button className="inner-button bg-transparent p-0 border-0 cursor-pointer font-normal text-[12px] text-[#747578] flex items-center">
                  Giá giảm dần
                  <FaSquareCaretDown className="text-[16px] ml-[5px]" />
                </button>
                <button className="inner-button active bg-transparent p-0 border-0 cursor-pointer font-normal text-[12px] text-[#F9313D] flex items-center">
                  Khuyến Mại Hot
                  <FaTag className="text-[16px] ml-[5px]" />
                </button>
                <button className="inner-button bg-transparent p-0 border-0 cursor-pointer font-normal text-[12px] text-[#747578] flex items-center">
                  Xem Nhiều
                  <FaEye className="text-[16px] ml-[5px]" />
                </button>
              </div>
              <div className="inner-total font-normal text-[14px] text-[#747578]">
                Tất cả: <b className="font-bold text-[14px]">101 Tour</b>
              </div>
              <button className="inner-filter-mobile lg:hidden inline-flex items-center bg-transparent border-0 gap-[6px] font-semibold text-[14px] text-[var(--color-primary)]">
                <div className="inner-title">
                  Bộ Lọc
                </div>
                <FaFilter className="text-[16px]" />
              </button>
            </div>
            <div className="inner-list-tour grid grid-cols-2 lg:grid-cols-3 gap-[10px] sm:gap-[20px]">
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
              <ProductItem />
            </div>
            <BoxPagination />
          </div>
        </div>
      </div>
    </div>
  );
};
