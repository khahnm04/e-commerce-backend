/* eslint-disable @next/next/no-img-element */
"use client";
import React from "react";
import { FaPlus, FaTrashCan } from "react-icons/fa6";

interface ImageRow {
  id?: number;
  imageUrl: string;
  displayOrder: number;
  file?: File;
  previewUrl?: string;
  isUploading?: boolean;
}

interface ProductImagesSectionProps {
  imageRows: ImageRow[];
  onImageRowsChange: (rows: ImageRow[]) => void;
  isReadOnly: boolean;
  initialImages?: { id: number; imageUrl: string; displayOrder: number }[];
}

export const ProductImagesSection = ({
  imageRows,
  onImageRowsChange,
  isReadOnly,
  initialImages,
}: ProductImagesSectionProps) => {
  return (
    <div className="md:col-span-2">
      <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
        Ảnh sản phẩm
      </label>
      {isReadOnly ? (
        <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px]">
          {initialImages && initialImages.length > 0 ? (
            <div className="flex flex-wrap gap-[12px]">
              {[...initialImages]
                .sort((a, b) => (a.displayOrder ?? 0) - (b.displayOrder ?? 0))
                .map((img, idx) => (
                  <div key={idx} className="relative">
                    <img
                      src={img.imageUrl}
                      alt={`Ảnh ${idx + 1}`}
                      className="w-[120px] h-[120px] object-cover rounded-[8px] border border-[#D5D5D5]"
                    />
                    <span className="absolute bottom-[4px] right-[4px] bg-black/60 text-white text-[11px] px-[6px] py-[2px] rounded-[4px]">
                      #{img.displayOrder ?? idx}
                    </span>
                  </div>
                ))}
            </div>
          ) : (
            <p className="text-gray-400 text-sm italic">Không có ảnh sản phẩm</p>
          )}
        </div>
      ) : (
        <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] space-y-[12px]">
          {imageRows.map((row, idx) => (
            <div key={idx} className="flex items-center gap-[12px]">
              {/* Preview */}
              {row.previewUrl ? (
                <img
                  src={row.previewUrl}
                  alt={`Ảnh ${idx + 1}`}
                  className="w-[60px] h-[60px] object-cover rounded-[6px] border border-[#D5D5D5] flex-shrink-0"
                />
              ) : (
                <div className="w-[60px] h-[60px] bg-[#E5E7EB] rounded-[6px] flex items-center justify-center text-gray-400 text-[11px] flex-shrink-0">
                  No img
                </div>
              )}
              {/* File input */}
              <div className="flex-1">
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => {
                    const file = e.target.files?.[0];
                    if (file) {
                      const updated = [...imageRows];
                      // Revoke previous blob URL to prevent memory leak
                      const prevUrl = updated[idx].previewUrl;
                      if (prevUrl && prevUrl.startsWith("blob:")) {
                        URL.revokeObjectURL(prevUrl);
                      }
                      updated[idx] = {
                        ...updated[idx],
                        file,
                        previewUrl: URL.createObjectURL(file),
                      };
                      onImageRowsChange(updated);
                    }
                  }}
                  className="bg-white border border-[#D5D5D5] rounded-[4px] px-[12px] py-[8px] font-[500] text-[13px] text-[var(--color-text)] w-full cursor-pointer file:mr-[10px] file:py-[4px] file:px-[12px] file:rounded-[4px] file:border-0 file:bg-blue-50 file:text-blue-700 file:font-[600] file:text-[12px] file:cursor-pointer"
                />
              </div>
              {/* Display order */}
              <div className="w-[80px]">
                <input
                  type="number"
                  value={row.displayOrder}
                  onChange={(e) => {
                    const updated = [...imageRows];
                    updated[idx] = { ...updated[idx], displayOrder: Number(e.target.value) };
                    onImageRowsChange(updated);
                  }}
                  placeholder="Thứ tự"
                  className="bg-white border border-[#D5D5D5] rounded-[4px] px-[10px] font-[500] text-[13px] text-[var(--color-text)] w-full h-[44px] outline-none focus:border-blue-400 transition-colors text-center"
                />
              </div>
              {/* Remove */}
              <button
                type="button"
                onClick={() => onImageRowsChange(imageRows.filter((_, i) => i !== idx))}
                className="text-[#EF3826] hover:bg-red-50 p-[10px] rounded-[6px] cursor-pointer transition-colors"
                title="Xóa ảnh"
              >
                <FaTrashCan className="text-[14px]" />
              </button>
            </div>
          ))}
          <button
            type="button"
            onClick={() => onImageRowsChange([...imageRows, { imageUrl: "", displayOrder: imageRows.length, previewUrl: "" }])}
            className="inline-flex items-center gap-[6px] text-[13px] font-[600] text-[var(--color-primary)] hover:bg-blue-50 px-[14px] py-[8px] rounded-[6px] cursor-pointer transition-colors border border-dashed border-blue-300"
          >
            <FaPlus className="text-[11px]" /> Thêm ảnh
          </button>
        </div>
      )}
    </div>
  );
};
