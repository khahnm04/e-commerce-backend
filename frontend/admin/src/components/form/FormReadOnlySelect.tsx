/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import React from "react";
import { Controller, Control } from "react-hook-form";
import { CustomSelect } from "@/components/common/CustomSelect";

interface Option {
  id: string | number;
  name: string;
}

interface FormReadOnlySelectProps {
  control: Control<any>;
  name: string;
  label: string;
  options: Option[];
  placeholder?: string;
  /** Text to show in read-only mode */
  readOnlyText?: string;
  isReadOnly?: boolean;
  error?: string;
  /** Value to set when nothing is selected. Defaults to 0 */
  emptyValue?: any;
}

/**
 * Reusable form select field with read-only support.
 * Wraps Controller + CustomSelect and handles the read-only display pattern.
 * Used for fields like brandId (ProductForm) and parentId (CategoryForm).
 */
export const FormReadOnlySelect = ({
  control,
  name,
  label,
  options,
  placeholder = "Chọn...",
  readOnlyText = "Không có",
  isReadOnly = false,
  error,
  emptyValue = 0,
}: FormReadOnlySelectProps) => {
  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <div className="inner-group relative">
          <label
            className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
            htmlFor={name}
          >
            {label}
          </label>
          {isReadOnly ? (
            <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] flex items-center opacity-60 cursor-not-allowed">
              {readOnlyText}
            </div>
          ) : (
            <CustomSelect
              options={options}
              value={field.value ? String(field.value) : ""}
              onChange={(val) => field.onChange(val ? Number(val) : emptyValue)}
              placeholder={placeholder}
              classNameCustom={`
                bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                w-full h-[52px] !block
              `}
            />
          )}
          {error && <p className="text-red-500 text-[13px] mt-[6px]">{error}</p>}
        </div>
      )}
    />
  );
};
