'use client';

import { useState, useEffect, useRef, useCallback } from 'react';
import { FaAngleDown, FaLocationDot, FaMagnifyingGlass } from 'react-icons/fa6';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface SuggestionItem {
  img: string;
  title: string;
  desc: string;
}

interface CounterType {
  key: 'adult' | 'child' | 'baby';
  label: string;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const SUGGESTIONS: SuggestionItem[] = [
  { img: '/assets/images/product-1.jpg', title: 'Nhật Bản', desc: '50 tours' },
  { img: '/assets/images/product-2.jpg', title: 'Phú Quốc', desc: '50 tours' },
  { img: '/assets/images/product-3.jpg', title: 'Singapore', desc: '50 tours' },
];

const COUNTER_TYPES: CounterType[] = [
  { key: 'adult', label: 'Người lớn' },
  { key: 'child', label: 'Trẻ em' },
  { key: 'baby', label: 'Em bé' },
];

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const SuggestionDropdown = ({
  isOpen,
  items,
  onSelect,
}: {
  isOpen: boolean;
  items: SuggestionItem[];
  onSelect: (title: string) => void;
}) => (
  <div
    className={`absolute top-full left-0 w-full z-[99] bg-white rounded-[10px] p-[16px_30px] ${isOpen ? 'block' : 'hidden'
      }`}
  >
    <div className="text-[var(--color-primary)] font-medium text-[14px] flex items-center gap-[9px] mb-[22px]">
      <FaLocationDot className="text-[17px]" /> Địa Điểm Hot
    </div>
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-[12px_30px]">
      {items.map(({ img, title, desc }) => (
        <button
          key={title}
          type="button"
          className="flex items-center gap-[16.5px] cursor-pointer bg-transparent border-0 p-0 text-left"
          onClick={() => onSelect(title)}
        >
          <div className="w-[99px] h-[58px] rounded-[5px] overflow-hidden shrink-0">
            <img src={img} alt={title} className="w-full h-full object-cover" />
          </div>
          <div className="flex-1">
            <div className="font-semibold text-[14px] text-[var(--color-primary)]">
              {title}
            </div>
            <div className="font-medium text-[12px] text-[#A6A6A6]">
              {desc}
            </div>
          </div>
        </button>
      ))}
    </div>
  </div>
);

const CounterDropdown = ({
  isOpen,
  counts,
  counterTypes,
  onCountChange,
}: {
  isOpen: boolean;
  counts: Record<'adult' | 'child' | 'baby', number>;
  counterTypes: CounterType[];
  onCountChange: (key: 'adult' | 'child' | 'baby', delta: number) => void;
}) => (
  <div
    className={`absolute top-full left-0 w-full z-[99] bg-white rounded-[10px] p-[12px_18px] ${isOpen ? 'block' : 'hidden'
      }`}
  >
    {counterTypes.map(({ key, label }) => (
      <div
        key={key}
        className="border-b border-[#ECECEC] pb-[6px] mb-[6px] flex items-center justify-between last:border-b-0 last:pb-0 last:mb-0"
      >
        <span className="font-normal text-[12px] text-[#A6A6A6]">{label}</span>
        <div className="inline-flex items-center gap-[10px]">
          <button
            type="button"
            aria-label={`Giảm ${label}`}
            className="border border-[#C0C0C0] rounded-[5px] bg-white w-[22px] h-[22px] inline-flex items-center justify-center font-normal text-[12px] text-[var(--color-primary)] cursor-pointer"
            onClick={() => onCountChange(key, -1)}
          >
            -
          </button>
          <span className="font-normal text-[12px] text-[var(--color-primary)] w-[15px] inline-flex justify-center">
            {counts[key]}
          </span>
          <button
            type="button"
            aria-label={`Tăng ${label}`}
            className="border border-[#C0C0C0] rounded-[5px] bg-white w-[22px] h-[22px] inline-flex items-center justify-center font-normal text-[12px] text-[var(--color-primary)] cursor-pointer"
            onClick={() => onCountChange(key, 1)}
          >
            +
          </button>
        </div>
      </div>
    ))}
  </div>
);

const SearchFieldGroup = ({
  icon,
  iconAlt,
  children,
  onClick,
}: {
  icon: string;
  iconAlt: string;
  children: React.ReactNode;
  onClick?: () => void;
}) => (
  <div
    className="bg-white rounded-[10px] p-[18px_12px] flex items-center cursor-pointer"
    onClick={onClick}
  >
    <img src={icon} className="h-[20px] w-auto mr-[14px]" alt={iconAlt} />
    {children}
    <FaAngleDown className="text-[10px] text-[var(--color-primary)]" />
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const HeroSearch = () => {
  const [activeBox, setActiveBox] = useState<string | null>(null);
  const [location, setLocation] = useState('');
  const [counts, setCounts] = useState({ adult: 1, child: 0, baby: 0 });
  const [isCountTouched, setIsCountTouched] = useState(false);
  const containerRef = useRef<HTMLFormElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        setActiveBox(null);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleCountChange = useCallback(
    (key: 'adult' | 'child' | 'baby', delta: number) => {
      setCounts((prev) => {
        const newValue = prev[key] + delta;
        if (newValue < 0) return prev;
        return { ...prev, [key]: newValue };
      });
      setIsCountTouched(true);
    },
    [],
  );

  const toggleBox = useCallback(
    (box: string) => setActiveBox((prev) => (prev === box ? null : box)),
    [],
  );

  const handleSuggestionSelect = useCallback((title: string) => {
    setLocation(title);
    setActiveBox(null);
  }, []);

  const categoryDisplayValue = isCountTouched
    ? `NL: ${counts.adult}, TE: ${counts.child}, EB: ${counts.baby}`
    : '';

  return (
    <section
      className="hero-search py-[106px] pb-[108px] bg-cover bg-center bg-no-repeat"
      style={{ backgroundImage: "url('/assets/images/background-1.jpg')" }}
    >
      <div className="container">
        <div className="w-full lg:w-[768px] mx-auto">
          <h1
            className="text-[var(--color-primary)] font-bold text-[27px] md:text-[30px] lg:text-[36px] leading-[50px] tracking-[1.8px] text-center mt-0 mb-[25px]"
            data-aos="fade-up"
            data-aos-duration="800"
          >
            Thế Giới Công Nghệ - Từ Phụ Kiện Đến Siêu Phẩm
          </h1>

          <form
            ref={containerRef}
            className="bg-[#00000066] rounded-[10px] p-[15px] lg:p-[40px] flex flex-wrap gap-[12px] lg:gap-[22px]"
            data-aos="fade-up"
            data-aos-duration="800"
            data-aos-delay="400"
            onSubmit={(e) => e.preventDefault()}
          >
            {/* Search input with suggestions */}
            <div className="w-full relative">
              <SearchFieldGroup
                icon="/assets/images/icon-address.svg"
                iconAlt="Tìm kiếm"
                onClick={() => toggleBox('address')}
              >
                <input
                  type="text"
                  placeholder="Bạn đang tìm sản phẩm gì?"
                  aria-label="Tìm kiếm sản phẩm"
                  className="flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                  value={location}
                  onChange={(e) => setLocation(e.target.value)}
                />
              </SearchFieldGroup>
              <SuggestionDropdown
                isOpen={activeBox === 'address'}
                items={SUGGESTIONS}
                onSelect={handleSuggestionSelect}
              />
            </div>

            {/* Category counter */}
            <div className="w-full md:w-[37.5%] relative">
              <SearchFieldGroup
                icon="/assets/images/icon-user.svg"
                iconAlt="Danh mục"
                onClick={() => toggleBox('user')}
              >
                <input
                  type="text"
                  placeholder="Danh mục"
                  aria-label="Danh mục"
                  className="flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                  value={categoryDisplayValue}
                  readOnly
                />
              </SearchFieldGroup>
              <CounterDropdown
                isOpen={activeBox === 'user'}
                counts={counts}
                counterTypes={COUNTER_TYPES}
                onCountChange={handleCountChange}
              />
            </div>

            {/* Price range */}
            <div className="w-full md:w-[37.5%] relative">
              <SearchFieldGroup
                icon="/assets/images/icon-calendar.svg"
                iconAlt="Mức giá"
              >
                <input
                  type="text"
                  placeholder="Mức giá"
                  aria-label="Mức giá"
                  className="flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                />
              </SearchFieldGroup>
            </div>

            {/* Search button */}
            <button
              type="submit"
              className="flex-1 bg-[var(--color-primary)] shadow-[0px_3.3px_3.3px_0px_#00000040] rounded-[10px] border-0 text-white flex justify-center items-center gap-[10px] font-medium text-[14px] h-[58px] lg:h-auto cursor-pointer hover:brightness-110 transition-all"
            >
              <FaMagnifyingGlass className="text-[20px]" /> Tìm Kiếm
            </button>
          </form>
        </div>
      </div>
    </section>
  );
};