"use client";
import { useMemo } from "react";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa6";
import { CustomSelect } from "./CustomSelect";
import { PageMetadata } from "@/types/common/api.types";

interface PaginationProps {
  meta?: PageMetadata;
  onPageChange?: (page: number) => void;
  onPageSizeChange?: (size: number) => void;
}

const PAGE_SIZE_OPTIONS = [
  { id: 10, name: "10 / Trang" },
  { id: 20, name: "20 / Trang" },
  { id: 50, name: "50 / Trang" },
  { id: 100, name: "100 / Trang" },
];

export const Pagination = ({ meta, onPageChange, onPageSizeChange }: PaginationProps) => {
  // Hooks must be called before any early returns (Rules of Hooks)
  const pageOptions = useMemo(
    () =>
      Array.from({ length: meta?.totalPages ?? 0 }, (_, i) => ({
        id: i + 1,
        name: `Trang ${i + 1}`,
      })),
    [meta?.totalPages],
  );

  if (!meta || meta.totalElements === 0) return null;

  const { pageNo, pageSize, totalElements, totalPages } = meta;

  const isFirst = pageNo <= 1;
  const isLast = pageNo >= totalPages;

  const startItem = (pageNo - 1) * pageSize + 1;
  const endItem = Math.min(pageNo * pageSize, totalElements);

  return (
    <div className="flex items-center gap-[16px] mt-[16px] flex-wrap">
      {/* Info */}
      <span className="font-[600] text-[14px] text-[var(--color-text)] opacity-[0.6]">
        Hiển thị {startItem} - {endItem} của {totalElements}
      </span>

      {/* Page size selector */}
      {onPageSizeChange && (
        <CustomSelect
          options={PAGE_SIZE_OPTIONS}
          value={pageSize}
          onChange={(val) => onPageSizeChange(Number(val))}
          classNameCustom="border border-[#D5D5D5] rounded-[8px] bg-[#FAFBFD] px-[8px] outline-none
            font-[600] text-[14px] text-[var(--color-text)] min-w-[120px] pl-[15px]"
        />
      )}

      {/* Page navigation */}
      <div className="flex items-center gap-[6px]">
        <button
          disabled={isFirst}
          onClick={() => onPageChange?.(pageNo - 1)}
          className={`w-[36px] h-[36px] flex items-center justify-center rounded-[8px] border border-[#D5D5D5] text-[18px] font-[600] transition-colors
            ${isFirst ? "opacity-40 cursor-not-allowed bg-[#F5F5F5] hover:bg-[#EAEAEA]" : "cursor-pointer bg-white hover:bg-[#F0F4FF]"}`}
        >
          <FaChevronLeft />
        </button>

        <CustomSelect
          options={pageOptions}
          value={pageNo}
          onChange={(val) => onPageChange?.(Number(val))}
          classNameCustom="border border-[#D5D5D5] rounded-[8px] bg-[#FAFBFD] px-[8px] outline-none
            font-[600] text-[14px] text-[var(--color-text)] min-w-[100px] pl-[15px]"
        />

        <button
          disabled={isLast}
          onClick={() => onPageChange?.(pageNo + 1)}
          className={`w-[36px] h-[36px] flex items-center justify-center rounded-[8px] border border-[#D5D5D5] text-[18px] font-[600] transition-colors
            ${isLast ? "opacity-40 cursor-not-allowed bg-[#F5F5F5] hover:bg-[#EAEAEA]" : "cursor-pointer bg-white hover:bg-[#F0F4FF]"}`}
        >
          <FaChevronRight />
        </button>
      </div>
    </div>
  );
};
