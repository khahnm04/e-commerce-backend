import { ReactNode } from "react"
import { FaFilter, FaRotateLeft } from "react-icons/fa6"

interface TableFilterProps {
  children?: ReactNode
}

export const TableFilter = ({ children }: TableFilterProps) => {
  return (
    <>
      <div className="inline-flex flex-wrap border border-[#D5D5D5] bg-[#FFFFFF] rounded-[14px] mb-[15px]">
        <div className="lg:px-[24px] lg:py-[12px] px-[15px] py-[10px] inline-flex items-center gap-[12px] border-r border-[#D5D5D5] font-[700] text-[14px] text-[var(--color-text)]">
          <FaFilter className="text-[22px]" /> Bộ lọc
        </div>

        {children}

        <div className="lg:px-[24px] lg:py-[12px] px-[15px] py-[10px] inline-flex items-center gap-[12px] border-[#D5D5D5] font-[700] text-[14px] text-[#EA0234] cursor-pointer">
          <FaRotateLeft className="text-[15px]" /> Xóa bộ lọc
        </div>
      </div>
    </>
  )
}

export const FilterItem = ({ children, className = "" }: { children: ReactNode, className?: string }) => {
  return (
    <div
      className={`lg:px-[24px] lg:py-[12px] px-[15px] py-[10px] w-[180px] flex justify-center items-center gap-[12px] 
        border-r border-[#D5D5D5] font-[700] text-[14px] text-[var(--color-text)] ${className}`}
    >
      {children}
    </div>
  );
};
