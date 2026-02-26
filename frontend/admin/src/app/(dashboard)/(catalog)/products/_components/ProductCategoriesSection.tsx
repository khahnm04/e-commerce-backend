"use client";
import React from "react";
import { Controller, Control } from "react-hook-form";

interface CategoryOption {
  id: number;
  name: string;
}

interface ProductCategoriesSectionProps {
  control: Control<any>;
  allCategories: CategoryOption[];
  isReadOnly: boolean;
  initialCategories?: { id: number; categoryName: string }[];
  errors: any;
}

export const ProductCategoriesSection = ({
  control,
  allCategories,
  isReadOnly,
  initialCategories,
  errors,
}: ProductCategoriesSectionProps) => {
  return (
    <Controller
      control={control}
      name="categoryIds"
      render={({ field }) => (
        <div className="md:col-span-2">
          <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">Danh mục</label>
          {isReadOnly ? (
            <div className="flex flex-wrap gap-[8px]">
              {initialCategories && initialCategories.length > 0 ? (
                initialCategories.map((cat) => (
                  <span
                    key={cat.id}
                    className="bg-blue-50 text-blue-700 border border-blue-200 rounded-[6px] px-[12px] py-[6px] text-[13px] font-[600]"
                  >
                    {cat.categoryName}
                  </span>
                ))
              ) : (
                <p className="text-gray-400 text-sm italic">Chưa gán danh mục</p>
              )}
            </div>
          ) : (
            <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] flex flex-wrap gap-[12px]">
              {allCategories.length === 0 ? (
                <p className="text-gray-400 text-sm italic">Đang tải danh sách danh mục...</p>
              ) : (
                allCategories.map((cat) => {
                  const isChecked = (field.value || []).includes(cat.id);
                  return (
                    <label
                      key={cat.id}
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
                            field.onChange([...current, cat.id]);
                          } else {
                            field.onChange(current.filter((id: number) => id !== cat.id));
                          }
                        }}
                      />
                      {cat.name}
                    </label>
                  );
                })
              )}
            </div>
          )}
          {errors?.categoryIds?.message && (
            <p className="text-red-500 text-[13px] mt-[6px]">
              {errors.categoryIds.message as string}
            </p>
          )}
        </div>
      )}
    />
  );
};
