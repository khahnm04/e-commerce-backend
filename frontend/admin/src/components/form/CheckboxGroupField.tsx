/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import React from "react";
import { Controller, Control, FieldErrors } from "react-hook-form";

interface CheckboxGroupFieldProps {
  control: Control<any>;
  name: string;
  label: string;
  items: { id: number; name: string }[];
  isReadOnly?: boolean;
  readOnlyLabels?: string[];
  emptyReadOnlyText?: string;
  loadingText?: string;
  errors?: FieldErrors;
}

export const CheckboxGroupField = ({
  control,
  name,
  label,
  items,
  isReadOnly = false,
  readOnlyLabels,
  emptyReadOnlyText = "Chưa có dữ liệu",
  loadingText = "Đang tải dữ liệu...",
  errors,
}: CheckboxGroupFieldProps) => {
  const fieldError = errors?.[name];

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <div className="md:col-span-2">
          <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">{label}</label>
          {isReadOnly ? (
            <div className="flex flex-wrap gap-[8px]">
              {readOnlyLabels && readOnlyLabels.length > 0 ? (
                readOnlyLabels.map((labelText, idx) => (
                  <span
                    key={idx}
                    className="bg-blue-50 text-blue-700 border border-blue-200 rounded-[6px] px-[12px] py-[6px] text-[13px] font-[600]"
                  >
                    {labelText}
                  </span>
                ))
              ) : (
                <p className="text-gray-400 text-sm italic">{emptyReadOnlyText}</p>
              )}
            </div>
          ) : (
            <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] flex flex-wrap gap-[12px]">
              {items.length === 0 ? (
                <p className="text-gray-400 text-sm italic">{loadingText}</p>
              ) : (
                items.map((item) => {
                  const isChecked = (field.value || []).includes(item.id);
                  return (
                    <label
                      key={item.id}
                      className={`
                        inline-flex items-center gap-[8px] cursor-pointer
                        border rounded-[6px] px-[14px] py-[8px]
                        text-[13px] font-[600] transition-all
                        ${
                          isChecked
                            ? "bg-blue-50 border-blue-400 text-blue-700"
                            : "bg-white border-[#D5D5D5] text-[#606060] hover:border-blue-300 hover:bg-blue-50/50"
                        }
                      `}
                    >
                      <input
                        type="checkbox"
                        checked={isChecked}
                        className="w-[16px] h-[16px] accent-blue-600 cursor-pointer"
                        onChange={(e) => {
                          const current = field.value || [];
                          if (e.target.checked) {
                            field.onChange([...current, item.id]);
                          } else {
                            field.onChange(current.filter((id: number) => id !== item.id));
                          }
                        }}
                      />
                      {item.name}
                    </label>
                  );
                })
              )}
            </div>
          )}
          {fieldError?.message && (
            <p className="text-red-500 text-[13px] mt-[6px]">{fieldError.message as string}</p>
          )}
        </div>
      )}
    />
  );
};
