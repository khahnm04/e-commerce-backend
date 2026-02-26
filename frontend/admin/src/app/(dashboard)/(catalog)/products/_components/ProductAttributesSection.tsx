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

const ReadOnlyAttributes = ({
  attributes,
}: {
  attributes?: { id: number; attributeName: string; value: string }[];
}) => {
  if (!attributes || attributes.length === 0) {
    return <p className="text-gray-400 text-sm italic">Chưa có thuộc tính</p>;
  }

  return (
    <div className="flex flex-wrap gap-[12px]">
      {attributes.map((attr, idx) => (
        <div
          key={idx}
          className="bg-white border border-[#D5D5D5] rounded-[6px] px-[14px] py-[8px] text-[13px]"
        >
          <span className="font-[600] text-[#606060]">{attr.attributeName}:</span>{" "}
          <span className="text-[var(--color-text)]">{attr.value}</span>
        </div>
      ))}
    </div>
  );
};

const EditableAttributeRow = ({
  row,
  index,
  allAttributes,
  onUpdate,
  onRemove,
}: {
  row: AttributeRow;
  index: number;
  allAttributes: AttributeOption[];
  onUpdate: (index: number, field: Partial<AttributeRow>) => void;
  onRemove: (index: number) => void;
}) => {
  const options = [
    { id: "", name: "Chọn thuộc tính" },
    ...allAttributes.map((a) => ({ id: String(a.id), name: a.name })),
  ];

  return (
    <div className="flex items-center gap-[12px]">
      <div className="flex-1">
        <CustomSelect
          options={options}
          value={row.attributeId ? String(row.attributeId) : ""}
          onChange={(val) => onUpdate(index, { attributeId: Number(val) })}
          placeholder="Chọn thuộc tính"
          classNameCustom="bg-white border border-[#D5D5D5] rounded-[4px] px-[12px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[44px] !block"
        />
      </div>
      <div className="flex-1">
        <input
          type="text"
          value={row.value}
          onChange={(e) => onUpdate(index, { value: e.target.value })}
          placeholder="Giá trị"
          className="bg-white border border-[#D5D5D5] rounded-[4px] px-[12px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[44px] outline-none focus:border-blue-400 transition-colors"
        />
      </div>
      <button
        type="button"
        onClick={() => onRemove(index)}
        className="text-[#EF3826] hover:bg-red-50 p-[10px] rounded-[6px] cursor-pointer transition-colors"
        title="Xóa thuộc tính"
      >
        <FaTrashCan className="text-[14px]" />
      </button>
    </div>
  );
};

export const ProductAttributesSection = ({
  attributeRows,
  onAttributeRowsChange,
  allAttributes,
  isReadOnly,
  initialAttributes,
}: ProductAttributesSectionProps) => {
  const handleUpdateRow = (index: number, field: Partial<AttributeRow>) => {
    const updated = [...attributeRows];
    updated[index] = { ...updated[index], ...field };
    onAttributeRowsChange(updated);
  };

  const handleRemoveRow = (index: number) => {
    onAttributeRowsChange(attributeRows.filter((_, i) => i !== index));
  };

  const handleAddRow = () => {
    onAttributeRowsChange([...attributeRows, { attributeId: 0, value: "" }]);
  };

  return (
    <div className="md:col-span-2">
      <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
        Thuộc tính sản phẩm
      </label>
      <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px]">
        {isReadOnly ? (
          <ReadOnlyAttributes attributes={initialAttributes} />
        ) : (
          <div className="space-y-[12px]">
            {attributeRows.map((row, idx) => (
              <EditableAttributeRow
                key={idx}
                row={row}
                index={idx}
                allAttributes={allAttributes}
                onUpdate={handleUpdateRow}
                onRemove={handleRemoveRow}
              />
            ))}
            <button
              type="button"
              onClick={handleAddRow}
              className="inline-flex items-center gap-[6px] text-[13px] font-[600] text-[var(--color-primary)] hover:bg-blue-50 px-[14px] py-[8px] rounded-[6px] cursor-pointer transition-colors border border-dashed border-blue-300"
            >
              <FaPlus className="text-[11px]" /> Thêm thuộc tính
            </button>
          </div>
        )}
      </div>
    </div>
  );
};
