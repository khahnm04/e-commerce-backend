'use client';

import { useState, useEffect, useRef } from 'react';
import { FaAngleDown, FaLocationDot, FaMagnifyingGlass } from 'react-icons/fa6';

export const Section1 = () => {
  const [activeBox, setActiveBox] = useState<string | null>(null);
  const [location, setLocation] = useState('');
  const [counts, setCounts] = useState({
    adult: 1,
    child: 0,
    baby: 0,
  });
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
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleCountChange = (type: 'adult' | 'child' | 'baby', delta: number) => {
    setCounts((prev) => {
      const newValue = prev[type] + delta;
      if (newValue < 0) return prev;
      return { ...prev, [type]: newValue };
    });
    setIsCountTouched(true);
  };

  const toggleBox = (box: string) => {
    if (activeBox === box) {
      setActiveBox(null);
    } else {
      setActiveBox(box);
    }
  };

  return (
    <>
      {/* Section 1 */}
      <div
        className="section-1 py-[106px] pb-[108px] bg-cover bg-center bg-no-repeat"
        style={{ backgroundImage: "url('/assets/images/background-1.jpg')" }}
      >
        <div className="container">
          <div className="inner-wrap w-full lg:w-[768px] mx-auto">
            <h1
              className="inner-title text-[var(--color-primary)] font-bold text-[27px] md:text-[30px] lg:text-[36px] leading-[50px] tracking-[1.8px] text-center mt-0 mb-[25px]"
              data-aos="fade-up"
              data-aos-duration="800"
            >
              Thế Giới Công Nghệ - Từ Phụ Kiện Đến Siêu Phẩm
            </h1>
            {/* <p
              className="inner-desc text-[#25282B] font-normal text-[14px] lg:text-[16px] leading-[100%] text-center mt-0 mb-[30px]"
              data-aos="fade-up"
              data-aos-duration="800"
              data-aos-delay="200"
            >
              Hơn 10.000+ sản phẩm chính hãng: Laptop, Điện thoại, Gaming Gear, Smarthome và đồ chơi công nghệ đang chờ bạn khám phá.
            </p> */}
            <form
              ref={containerRef}
              action="#"
              className="inner-form bg-[#00000066] rounded-[10px] p-[15px] lg:p-[40px] flex flex-wrap gap-[12px] lg:gap-[22px]"
              data-aos="fade-up"
              data-aos-duration="800"
              data-aos-delay="400"
              onSubmit={(e) => e.preventDefault()}
            >
              <div
                className={`inner-box inner-address w-full relative ${activeBox === 'address' ? 'active' : ''
                  }`}
              >
                <div
                  className="inner-input-group bg-white rounded-[10px] p-[18px_12px] flex items-center cursor-pointer"
                  onClick={() => toggleBox('address')}
                >
                  <img
                    src="/assets/images/icon-address.svg"
                    className="inner-icon h-[20px] w-auto mr-[14px]"
                    alt="Address Icon"
                  />
                  <input
                    type="text"
                    placeholder="Bạn đang tìm sản phẩm gì?"
                    className="inner-input flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                    value={location}
                    onChange={(e) => setLocation(e.target.value)}
                  />
                  <FaAngleDown className="inner-down text-[10px] text-[var(--color-primary)]" />
                </div>
                <div
                  className={`inner-suggest absolute top-full left-0 w-full z-[99] bg-white rounded-[10px] p-[16px_30px] ${activeBox === 'address' ? 'block' : 'hidden'
                    }`}
                >
                  <div className="inner-suggest-title text-[var(--color-primary)] font-medium text-[14px] flex items-center gap-[9px] mb-[22px]">
                    <FaLocationDot className="text-[17px]" /> Địa Điểm Hot
                  </div>
                  <div className="inner-suggest-list grid grid-cols-1 sm:grid-cols-2 gap-[12px_30px]">
                    {[
                      {
                        img: '/assets/images/product-1.jpg',
                        title: 'Nhật Bản',
                        desc: '50 tours',
                      },
                      {
                        img: '/assets/images/product-2.jpg',
                        title: 'Phú Quốc',
                        desc: '50 tours',
                      },
                      {
                        img: '/assets/images/product-3.jpg',
                        title: 'Singapore',
                        desc: '50 tours',
                      },
                    ].map((item, index) => (
                      <div
                        key={index}
                        className="inner-item flex items-center gap-[16.5px] cursor-pointer"
                        onClick={() => {
                          setLocation(item.title);
                          setActiveBox(null);
                        }}
                      >
                        <div className="inner-item-image w-[99px] h-[58px] rounded-[5px] overflow-hidden">
                          <img
                            src={item.img}
                            alt={item.title}
                            className="w-full h-full object-cover"
                          />
                        </div>
                        <div className="inner-item-content flex-1">
                          <div className="inner-item-title font-semibold text-[14px] text-[var(--color-primary)]">
                            {item.title}
                          </div>
                          <div className="inner-item-desc font-medium text-[12px] text-[#A6A6A6]">
                            {item.desc}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>

              <div
                className={`inner-box inner-user w-full md:w-[37.5%] relative ${activeBox === 'user' ? 'active' : ''
                  }`}
              >
                <div
                  className="inner-input-group bg-white rounded-[10px] p-[18px_12px] flex items-center cursor-pointer"
                  onClick={() => toggleBox('user')}
                >
                  <img
                    src="/assets/images/icon-user.svg"
                    className="inner-icon h-[20px] w-auto mr-[14px]"
                    alt="User Icon"
                  />
                  <input
                    type="text"
                    placeholder="Danh mục"
                    className="inner-input flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                    value={
                      isCountTouched
                        ? `NL: ${counts.adult}, TE: ${counts.child}, EB: ${counts.baby}`
                        : ''
                    }
                    readOnly
                  />
                  <FaAngleDown className="inner-down text-[10px] text-[var(--color-primary)]" />
                </div>
                <div
                  className={`inner-quantity absolute top-full left-0 w-full z-[99] bg-white rounded-[10px] p-[12px_18px] ${activeBox === 'user' ? 'block' : 'hidden'
                    }`}
                >
                  {[
                    { label: 'Người lớn', type: 'adult' as const },
                    { label: 'Trẻ em', type: 'child' as const },
                    { label: 'Em bé', type: 'baby' as const },
                  ].map((item, index) => (
                    <div
                      key={index}
                      className="inner-item border-b border-[#ECECEC] pb-[6px] mb-[6px] flex items-center justify-between last:border-b-0 last:pb-0 last:mb-0"
                    >
                      <div className="inner-label font-normal text-[12px] text-[#A6A6A6]">
                        {item.label}
                      </div>
                      <div className="inner-count inline-flex items-center gap-[10px]">
                        <span
                          className="inner-down border border-[#C0C0C0] rounded-[5px] bg-white w-[22px] h-[22px] inline-flex items-center justify-center font-normal text-[12px] text-[var(--color-primary)] cursor-pointer"
                          onClick={() => handleCountChange(item.type, -1)}
                        >
                          -
                        </span>
                        <span className="inner-number font-normal text-[12px] text-[var(--color-primary)] w-[15px] inline-flex justify-center">
                          {counts[item.type]}
                        </span>
                        <span
                          className="inner-up border border-[#C0C0C0] rounded-[5px] bg-white w-[22px] h-[22px] inline-flex items-center justify-center font-normal text-[12px] text-[var(--color-primary)] cursor-pointer"
                          onClick={() => handleCountChange(item.type, 1)}
                        >
                          +
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="inner-box inner-calendar w-full md:w-[37.5%] relative">
                <div className="inner-input-group bg-white rounded-[10px] p-[18px_12px] flex items-center">
                  <img
                    src="/assets/images/icon-calendar.svg"
                    className="inner-icon h-[20px] w-auto mr-[14px]"
                    alt="Calendar Icon"
                  />
                  <input
                    type="text"
                    placeholder="Mức giá"
                    className="inner-input flex-1 outline-none border-0 font-normal text-[14px] tracking-[0.6px] placeholder-[#A6A6A6]"
                  />
                  <FaAngleDown className="inner-down text-[10px] text-[var(--color-primary)]" />
                </div>
              </div>

              <button className="inner-button flex-1 bg-[var(--color-primary)] shadow-[0px_3.3px_3.3px_0px_#00000040] rounded-[10px] border-0 text-white flex justify-center items-center gap-[10px] font-medium text-[14px] h-[58px] lg:h-auto">
                <FaMagnifyingGlass className="text-[20px]" /> Tìm Kiếm
              </button>
            </form>
          </div>
        </div>
      </div>
      {/* End Section 1 */}
    </>
  );
};