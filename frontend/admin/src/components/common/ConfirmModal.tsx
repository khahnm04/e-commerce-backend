"use client";
import React from "react";
import { createPortal } from "react-dom";

interface ConfirmModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title: string;
  message: string;
}

export const ConfirmModal = ({ isOpen, onClose, onConfirm, title, message }: ConfirmModalProps) => {
  if (!isOpen) return null;

  return createPortal(
    <div
      className="fixed inset-0 z-[9999] flex items-center justify-center
        bg-gray-900/50 backdrop-blur-sm p-[16px]"
      onClick={onClose}
    >
      <div
        className="relative w-[480px] h-[206px] bg-white rounded-[12px]
          shadow-2xl flex flex-col animate-in fade-in zoom-in duration-200"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="pt-[32px] px-[32px] text-left">
          <h2 className="text-[20px] font-bold text-[#111827] mb-[8px] leading-[24px]">{title}</h2>
          <p className="text-[16px] text-[#6B7280] leading-[20px] pr-[20px]">{message}</p>
        </div>
        <div className="mt-auto pb-[24px] px-[32px] flex justify-end gap-[12px]">
          <button
            type="button"
            className="w-[84px] h-[48px] flex items-center justify-center
              rounded-[8px] border border-[#D1D5DB] text-[#374151] text-[16px]
              font-semibold hover:bg-[#F9FAFB] transition-all active:scale-95
              cursor-pointer"
            onClick={onClose}
          >
            Hủy
          </button>
          <button
            type="button"
            className="w-[135px] h-[48px] flex items-center justify-center
              rounded-[8px] bg-[#EF3826] text-white text-[16px] font-semibold
              hover:bg-[#D32F2F] shadow-sm transition-all active:scale-95
              cursor-pointer"
            onClick={onConfirm}
          >
            Xác nhận
          </button>
        </div>
      </div>
    </div>,
    document.body,
  );
};
