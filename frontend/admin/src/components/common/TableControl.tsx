"use client";
import React from "react";
import { CustomSelect } from "@/components/common/CustomSelect";
import { DynamicTableFilter, FilterConfig } from "@/components/common/DynamicTableFilter";
import { TableAction } from "@/components/common/TableAction";

interface TableControlProps {
  // Cấu hình chung
  isTrash?: boolean;
  createLink?: string;
  createLabel?: string;
  trashLink?: string;

  // Search
  searchTerm: string;
  onSearchChange: (value: string) => void;

  // Filters
  filterList?: FilterConfig[];
  onClearFilter?: () => void;

  // Bulk Actions
  actionFilter: string | number;
  onActionFilterChange: (value: string | number) => void;
  actionOptions: { id: string | number; name: string }[];
  onApplyAction?: () => void;
}

export const TableControl = ({
  isTrash = false,
  createLink,
  createLabel = "Tạo mới",
  trashLink,
  searchTerm,
  onSearchChange,
  filterList = [],
  onClearFilter,
  actionFilter,
  onActionFilterChange,
  actionOptions,
  onApplyAction = () => {},
}: TableControlProps) => {
  return (
    <>
      {!isTrash && filterList.length > 0 && (
        <DynamicTableFilter filters={filterList} onClearFilter={onClearFilter} />
      )}

      <TableAction
        searchValue={searchTerm}
        onSearchChange={onSearchChange}
        createLink={isTrash ? undefined : createLink}
        createLabel={isTrash ? undefined : createLabel}
        trashLink={isTrash ? undefined : trashLink}
      >
        <div
          className="lg:px-[24px] lg:py-[12px] px-[15px] py-[10px] w-[180px]
            flex justify-center items-center gap-[12px] border-r
            border-[#D5D5D5] font-[700] text-[14px] text-[var(--color-text)]"
        >
          <CustomSelect
            value={actionFilter}
            onChange={onActionFilterChange}
            options={actionOptions}
            placeholder="Hành động"
          />
        </div>
        <div
          className="lg:px-[24px] lg:py-[12px] px-[15px] py-[10px]
            inline-flex items-center gap-[12px] border-[#D5D5D5]
            font-[700] text-[14px] text-[var(--color-text)]"
        >
          <button
            className="bg-transparent border-0 font-[600] text-[14px]
              text-[#EA0234] cursor-pointer hover:underline"
            onClick={onApplyAction}
          >
            Áp dụng
          </button>
        </div>
      </TableAction>
    </>
  );
};
