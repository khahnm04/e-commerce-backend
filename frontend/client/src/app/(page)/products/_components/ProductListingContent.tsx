'use client';

import { useState, useCallback } from 'react';
import { IconType } from 'react-icons';
import { FaEye, FaFilter, FaSquareCaretDown, FaSquareCaretUp, FaTag } from 'react-icons/fa6';
import { BoxFilter } from '@/components/common/BoxFilter';
import { ProductItem } from '@/components/product/ProductItem';
import { BoxPagination } from '@/components/common/BoxPagination';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface SortOption {
  key: string;
  label: string;
  icon: IconType;
}

interface CategoryInfo {
  title: string;
  description: string;
  totalProducts: number;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const SORT_OPTIONS: SortOption[] = [
  { key: 'price_asc', label: 'Giá tăng dần', icon: FaSquareCaretUp },
  { key: 'price_desc', label: 'Giá giảm dần', icon: FaSquareCaretDown },
  { key: 'hot_deals', label: 'Khuyến Mại Hot', icon: FaTag },
  { key: 'most_viewed', label: 'Xem Nhiều', icon: FaEye },
];

const CATEGORY_INFO: CategoryInfo = {
  title: 'Tour Nước Ngoài',
  description:
    'Du lịch Châu Á: là châu lục lớn và đông dân nhất thế giới. Châu Á cũng có nhiều danh lam thắng cảnh đẹp tại một số quốc gia như Hàn Quốc, Nhật Bản, Trung Quốc, Việt Nam,... và VK Travel là nhà tổ chức du lịch chuyên nghiệp hàng đầu Châu Á, đã 5 lần liên tiếp nhận giải TTG Travel Awards. Hãy cùng 28.Travel du lịch Châu Á để tận hưởng những dịch vụ tốt nhất.',
  totalProducts: 101,
};

const PRODUCT_COUNT = 9;

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const CategoryHeader = ({ title, description }: Omit<CategoryInfo, 'totalProducts'>) => (
  <div className="mb-[16px]">
    <h2 className="mt-0 mb-[10px] font-semibold text-[24px] sm:text-[28px] text-[var(--color-primary)]">
      {title}
    </h2>
    <p className="m-0 font-normal text-[14px] text-[#747578] text-justify">
      {description}
    </p>
  </div>
);

const SortBar = ({
  options,
  activeKey,
  onSortChange,
  totalProducts,
  onToggleMobileFilter,
}: {
  options: SortOption[];
  activeKey: string;
  onSortChange: (key: string) => void;
  totalProducts: number;
  onToggleMobileFilter: () => void;
}) => (
  <div className="flex items-center justify-between flex-wrap mb-[36px]">
    <div className="flex gap-[20px_16px] sm:gap-[16px] w-full lg:w-auto mb-[25px] lg:mb-0 flex-wrap sm:flex-nowrap">
      <span className="font-semibold text-[14px] text-[#747578] flex items-center">
        Sắp xếp :
      </span>
      {options.map(({ key, label, icon: Icon }) => {
        const isActive = activeKey === key;
        return (
          <button
            key={key}
            type="button"
            onClick={() => onSortChange(key)}
            className={`bg-transparent p-0 border-0 cursor-pointer font-normal text-[12px] flex items-center transition-colors ${isActive ? 'text-[#F9313D] font-medium' : 'text-[#747578] hover:text-[var(--color-primary)]'
              }`}
          >
            {label}
            <Icon className="text-[16px] ml-[5px]" />
          </button>
        );
      })}
    </div>

    <p className="m-0 font-normal text-[14px] text-[#747578]">
      Tất cả: <b className="font-bold text-[14px]">{totalProducts} Tour</b>
    </p>

    <button
      type="button"
      onClick={onToggleMobileFilter}
      className="lg:hidden inline-flex items-center bg-transparent border-0 gap-[6px] font-semibold text-[14px] text-[var(--color-primary)] cursor-pointer"
    >
      Bộ Lọc
      <FaFilter className="text-[16px]" />
    </button>
  </div>
);

const ProductGrid = ({ count }: { count: number }) => (
  <div className="grid grid-cols-2 lg:grid-cols-3 gap-[10px] sm:gap-[20px]">
    {Array.from({ length: count }, (_, index) => (
      <ProductItem key={index} />
    ))}
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const ProductListingContent = () => {
  const [activeSort, setActiveSort] = useState('hot_deals');
  const [isMobileFilterOpen, setIsMobileFilterOpen] = useState(false);

  const handleSortChange = useCallback((key: string) => {
    setActiveSort(key);
  }, []);

  const handleToggleMobileFilter = useCallback(() => {
    setIsMobileFilterOpen((prev) => !prev);
  }, []);

  return (
    <section className="product-listing py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        <div className="flex">
          {/* Sidebar filter — Desktop */}
          <aside className="w-[23.5%] mr-[23px] hidden lg:block lg:relative lg:z-[1]">
            <BoxFilter />
          </aside>

          {/* Sidebar filter — Mobile overlay */}
          {isMobileFilterOpen && (
            <div className="fixed inset-0 z-50 lg:hidden">
              <div
                className="absolute inset-0 bg-black/40"
                onClick={handleToggleMobileFilter}
              />
              <div className="absolute top-0 left-0 w-[85%] max-w-[360px] h-full bg-white overflow-y-auto p-[15px] animate-slideIn">
                <BoxFilter />
              </div>
            </div>
          )}

          {/* Main content */}
          <div className="flex-1">
            <CategoryHeader
              title={CATEGORY_INFO.title}
              description={CATEGORY_INFO.description}
            />

            <SortBar
              options={SORT_OPTIONS}
              activeKey={activeSort}
              onSortChange={handleSortChange}
              totalProducts={CATEGORY_INFO.totalProducts}
              onToggleMobileFilter={handleToggleMobileFilter}
            />

            <ProductGrid count={PRODUCT_COUNT} />

            <BoxPagination />
          </div>
        </div>
      </div>
    </section>
  );
};
