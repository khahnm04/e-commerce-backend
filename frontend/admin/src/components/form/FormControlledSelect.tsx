/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import React from "react";
import { Controller, Control } from "react-hook-form";
import { CustomSelect } from "@/components/common/CustomSelect";

interface Option {
  id: string | number;
  name: string;
}

interface FormControlledSelectProps {
  control: Control<any>;
  name: string;
  label: string;
  options: Option[];
  placeholder?: string;
  disabled?: boolean;
}

/**
 * Reusable form select field that wraps Controller + CustomSelect.
 * Eliminates ~20 lines of boilerplate per usage.
 */
export const FormControlledSelect = ({
  control,
  name,
  label,
  options,
  placeholder = "Chọn...",
  disabled = false,
}: FormControlledSelectProps) => {
  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <div className="inner-group relative">
          <label
            className="inner-label block font-[600] text-[14px]
              text-[#606060] mb-[11px]"
            htmlFor={name}
          >
            {label}
          </label>
          <CustomSelect
            options={options}
            value={field.value ?? ""}
            onChange={(val) => field.onChange(val)}
            placeholder={placeholder}
            disabled={disabled}
            classNameCustom={`
              bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
              px-[22px] font-[500] text-[14px] text-[var(--color-text)]
              w-full h-[52px] !block
              ${disabled ? "opacity-60 cursor-not-allowed" : ""}
            `}
          />
        </div>
      )}
    />
  );
};
