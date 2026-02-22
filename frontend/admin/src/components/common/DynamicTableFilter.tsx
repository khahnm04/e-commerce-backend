"use client";
import { ReactNode } from "react";
import { CustomSelect } from "./CustomSelect";
import { FilterItem, TableFilter } from "./TableFilter";

export type FilterConfig = {
  type: "select" | "date" | "input" | "custom";
  id: string;
  className?: string;
  value?: any;
  onChange?: (val: any) => void;
  options?: any[];
  placeholder?: string;
  render?: () => ReactNode;
};

interface DynamicTableFilterProps {
  filters: FilterConfig[];
}

export const DynamicTableFilter = ({
  filters
}: DynamicTableFilterProps) => {
  return (
    <>
      <TableFilter>
        {filters.map((item: any) => (
          <FilterItem
            key={item.id}
            className={item.className}
          >
            {item.type === "select" && (
              <CustomSelect
                value={item.value}
                onChange={item.onChange}
                options={item.options}
                placeholder={item.placeholder}
              />
            )}
            {item.type === "date" && (
              <>
                <input type="date" className="border-0 outline-none font-[700] text-[14px] text-[var(--color-text)] cursor-pointer w-[110px]" />
                <span>-</span>
                <input type="date" className="border-0 outline-none font-[700] text-[14px] text-[var(--color-text)] cursor-pointer w-[110px]" />
              </>
            )}
            {item.type === "custom" && item.render && item.render()}
          </FilterItem>
        ))}
      </TableFilter>
    </>
  );
};
