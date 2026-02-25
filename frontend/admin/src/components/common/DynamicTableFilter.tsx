"use client";
import { ReactNode } from "react";
import { CustomSelect } from "./CustomSelect";
import { FilterItem, TableFilter } from "./TableFilter";

interface SelectOption {
  id: string | number;
  name: string;
}

export type FilterConfig = {
  type: "select" | "date" | "input" | "custom";
  id: string;
  className?: string;
  value?: string | number;
  onChange?: (val: string | number) => void;
  options?: SelectOption[];
  placeholder?: string;
  render?: () => ReactNode;
  // Date range fields
  startValue?: string;
  endValue?: string;
  onStartChange?: (val: string) => void;
  onEndChange?: (val: string) => void;
};

interface DynamicTableFilterProps {
  filters: FilterConfig[];
  onClearFilter?: () => void;
}

export const DynamicTableFilter = ({
  filters,
  onClearFilter
}: DynamicTableFilterProps) => {
  return (
    <>
      <TableFilter onClearFilter={onClearFilter}>
        {filters.map((item: FilterConfig) => (
          <FilterItem
            key={item.id}
            className={item.className}
          >
            {item.type === "select" && (
              <CustomSelect
                value={item.value ?? ""}
                onChange={item.onChange ?? (() => { })}
                options={item.options ?? []}
                placeholder={item.placeholder}
              />
            )}
            {item.type === "date" && (
              <>
                <input
                  type="date"
                  className="border-0 outline-none font-[700] text-[14px] text-[var(--color-text)] cursor-pointer w-[110px]"
                  value={item.startValue ?? ""}
                  onChange={(e) => item.onStartChange?.(e.target.value)}
                />
                <span>-</span>
                <input
                  type="date"
                  className="border-0 outline-none font-[700] text-[14px] text-[var(--color-text)] cursor-pointer w-[110px]"
                  value={item.endValue ?? ""}
                  onChange={(e) => item.onEndChange?.(e.target.value)}
                />
              </>
            )}
            {item.type === "custom" && item.render && item.render()}
          </FilterItem>
        ))}
      </TableFilter>
    </>
  );
};
