/* eslint-disable @next/next/no-img-element */
"use client";
import React from "react";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FaPlus, FaTrashCan } from "react-icons/fa6";

interface AttributeRow {
  id?: number;
  attributeId: number;
  value: string;
}

interface AttributeOption {
  id: number;
  name: string;
}

interface ProductAttributesSectionProps {
  attributeRows: AttributeRow[];
  onAttributeRowsChange: (rows: AttributeRow[]) => void;
  allAttributes: AttributeOption[];
  isReadOnly: boolean;
  initialAttributes?: { id: number; attributeName: string; value: string }[];
}

export const ProductAttributesSection = ({
  attributeRows,
  onAttributeRowsChange,
  allAttributes,
  isReadOnly,
  initialAttributes,
}: ProductAttributesSectionProps) => {
  return (
    <div className="md:col-span-2">
      <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
        Thuộc tính sản phẩm
      </label>
      {isReadOnly ? (
        <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px]">
          {initialAttributes && initialAttributes.length > 0 ? (
            <div className="flex flex-wrap gap-[12px]">
              {initialAttributes.map((attr, idx) => (
                <div
                  key={idx}
                  className="bg-white border border-[#D5D5D5] rounded-[6px] px-[14px] py-[8px] text-[13px]"
                >
                  <span className="font-[600] text-[#606060]">{attr.attributeName}:</span>{" "}
                  <span className="text-[var(--color-text)]">{attr.value}</span>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-gray-400 text-sm italic">Chưa có thuộc tính</p>
          )}
        </div>
      ) : (
        <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] space-y-[12px]">
          {attributeRows.map((row, idx) => (
            <div key={idx} className="flex items-center gap-[12px]">
              <div className="flex-1">
                <CustomSelect
                  options={[
                    { id: "", name: "Chọn thuộc tính" },
                    ...allAttributes.map((a) => ({ id: String(a.id), name: a.name })),
                  ]}
                  value={row.attributeId ? String(row.attributeId) : ""}
                  onChange={(val) => {
                    const updated = [...attributeRows];
                    updated[idx] = { ...updated[idx], attributeId: Number(val) };
                    onAttributeRowsChange(updated);
                  }}
                  placeholder="Chọn thuộc tính"
                  classNameCustom="bg-white border border-[#D5D5D5] rounded-[4px] px-[12px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[44px] !block"
                />
              </div>
              <div className="flex-1">
                <input
                  type="text"
                  value={row.value}
                  onChange={(e) => {
                    const updated = [...attributeRows];
                    updated[idx] = { ...updated[idx], value: e.target.value };
                    onAttributeRowsChange(updated);
                  }}
                  placeholder="Giá trị"
                  className="bg-white border border-[#D5D5D5] rounded-[4px] px-[12px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[44px] outline-none focus:border-blue-400 transition-colors"
                />
              </div>
              <button
                type="button"
                onClick={() => onAttributeRowsChange(attributeRows.filter((_, i) => i !== idx))}
                className="text-[#EF3826] hover:bg-red-50 p-[10px] rounded-[6px] cursor-pointer transition-colors"
                title="Xóa thuộc tính"
              >
                <FaTrashCan className="text-[14px]" />
              </button>
            </div>
          ))}
          <button
            type="button"
            onClick={() => onAttributeRowsChange([...attributeRows, { attributeId: 0, value: "" }])}
            className="inline-flex items-center gap-[6px] text-[13px] font-[600] text-[var(--color-primary)] hover:bg-blue-50 px-[14px] py-[8px] rounded-[6px] cursor-pointer transition-colors border border-dashed border-blue-300"
          >
            <FaPlus className="text-[11px]" /> Thêm thuộc tính
          </button>
        </div>
      )}
    </div>
  );
};
