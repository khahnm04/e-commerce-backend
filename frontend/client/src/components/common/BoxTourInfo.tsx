"use client"
import { useState } from 'react';

export const BoxTourInfo = () => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className={`box-tour-info bg-white shadow-[0px_1.75px_3.51px_0px_#0000001F] rounded-[10px] p-[15px] sm:p-[20px] ${isExpanded ? 'active' : ''}`}>
      <div className="inner-title mb-[15px] font-semibold text-[20px] text-[var(--color-primary)]">
        Thông Tin Tour
      </div>
      <div className={`inner-content ${isExpanded ? 'h-auto overflow-visible' : 'h-[500px] overflow-hidden'}`}>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem, ipsum dolor sit amet consectetur adipisicing elit. Aspernatur consequatur quae odio totam non facilis, ipsa quibusdam dolorum reiciendis cumque quos minus. At, culpa corporis facilis deleniti et molestiae tempore?
        </p>
        <img
          src="/assets/images/banner-13.jpg"
          alt="Tour Info Banner"
          className="w-full h-auto rounded-[10px] mb-[10px]"
        />
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum dolor, sit amet consectetur adipisicing elit. Quasi odio laboriosam ipsum modi incidunt! Ex aspernatur autem, repudiandae corrupti esse pariatur tempora sapiente ullam asperiores, facere dolorem quaerat saepe! Quis.
        </p>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quas cupiditate sequi tempore quidem, reprehenderit illo enim ducimus qui deleniti. Doloribus impedit itaque eaque maiores alias, explicabo veritatis excepturi. Quasi, distinctio.
        </p>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quas cupiditate sequi tempore quidem, reprehenderit illo enim ducimus qui deleniti. Doloribus impedit itaque eaque maiores alias, explicabo veritatis excepturi. Quasi, distinctio.
        </p>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quas cupiditate sequi tempore quidem, reprehenderit illo enim ducimus qui deleniti. Doloribus impedit itaque eaque maiores alias, explicabo veritatis excepturi. Quasi, distinctio.
        </p>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quas cupiditate sequi tempore quidem, reprehenderit illo enim ducimus qui deleniti. Doloribus impedit itaque eaque maiores alias, explicabo veritatis excepturi. Quasi, distinctio.
        </p>
        <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
          Lorem ipsum, dolor sit amet consectetur adipisicing elit. Quas cupiditate sequi tempore quidem, reprehenderit illo enim ducimus qui deleniti. Doloribus impedit itaque eaque maiores alias, explicabo veritatis excepturi. Quasi, distinctio.
        </p>
      </div>
      <div className="inner-read-more text-center mt-[20px]">
        <button
          className="button-outline border border-[var(--color-primary)] text-[var(--color-primary)] bg-transparent rounded-[8px] px-[24px] py-[10px] font-medium text-[14px] hover:bg-[var(--color-primary)] hover:text-white transition-all cursor-pointer"
          onClick={() => setIsExpanded(!isExpanded)}
        >
          {isExpanded ? 'Thu gọn' : 'Xem tất cả'}
        </button>
      </div>
    </div>
  );
};
