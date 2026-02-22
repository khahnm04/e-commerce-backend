"use client"
import { useState } from "react";
import { CustomSelect } from "./CustomSelect";

export const Pagination = () => {
  const [currentPage, setCurrentPage] = useState<string | number>(1);

  const options = [
    { id: 1, name: "Trang 1" },
    { id: 2, name: "Trang 2" },
    { id: 3, name: "Trang 3" },
  ];

  return (
    <>
      <div className="flex items-center gap-[20px]">
        <span className="font-[600] text-[14px] text-[var(--color-text)] opacity-[0.6]">
          Hiển thị 1 - 9 của 78
        </span>
        <CustomSelect
          options={options}
          value={currentPage}
          onChange={(val) => setCurrentPage(val)}
          classNameCustom="border border-[#D5D5D5] rounded-[8px] bg-[#FAFBFD] px-[8px] outline-none
           font-[600] text-[14px] text-[var(--color-text)] min-w-[100px] pl-[15px]"
        />
      </div>
    </>
  );
};
