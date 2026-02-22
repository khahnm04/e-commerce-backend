"use client";
import Link from "next/link";
import { ReactNode } from "react";
import { FaMagnifyingGlass } from "react-icons/fa6";

interface TableActionProps {
  searchValue?: string;
  onSearchChange?: (val: string) => void;
  searchPlaceholder?: string;
  createLink?: string;
  createLabel?: string;
  trashLink?: string;
  children?: ReactNode;
}

export const TableAction = ({
  searchValue,
  onSearchChange,
  searchPlaceholder = "Tìm kiếm",
  createLink,
  createLabel = "Tạo mới",
  trashLink,
  children,
}: TableActionProps) => {
  return (
    <div className="flex flex-wrap gap-[20px] mb-[30px]">

      {/* Hành động (Dropdown + Button Áp dụng) */}
      {children && (
        <div className="inline-flex flex-wrap border border-[#D5D5D5] bg-[#FFFFFF] rounded-[14px]">
          {children}
        </div>
      )}

      {/* Tìm kiếm */}
      <div className="border border-[#E2E2E2] bg-[#fff] inline-flex items-center gap-[15px] max-w-[366px] w-full lg:px-[24px] lg:py-[12px] px-[15px] py-[10px] rounded-[14px]">
        <FaMagnifyingGlass className="text-[20px]" />
        <input
          type="text"
          value={searchValue}
          onChange={(e) => onSearchChange && onSearchChange(e.target.value)}
          placeholder={searchPlaceholder}
          className="text-[14px] font-[700] flex-1 border-0 outline-none text-[var(--color-text)] placeholder:text-[#979797]"
        />
      </div>

      {/* Tạo mới */}
      {createLink && (
        <div className="inline-flex items-center justify-center border border-transparent bg-[var(--color-primary)] rounded-[14px] lg:px-[50px] px-[30px] lg:py-[12px] py-[10px] cursor-pointer">
          <Link
            href={createLink}
            className="font-[700] text-[14px] text-white whitespace-nowrap"
          >
            + {createLabel}
          </Link>
        </div>
      )}

      {/* Thùng rác */}
      {trashLink && (
        <div className="inline-flex items-center justify-center border border-transparent bg-[#EF382633] rounded-[14px] px-[30px] lg:py-[12px] py-[10px] cursor-pointer">
          <Link
            href={trashLink}
            className="font-[700] text-[14px] text-[#EF3826] whitespace-nowrap"
          >
            Thùng rác
          </Link>
        </div>
      )}
    </div>
  );
};
