import React from "react";

interface LoadingSpinnerProps {
  text?: string;
  className?: string;
}

export const LoadingSpinner = ({
  text = "Đang tải dữ liệu...",
  className = "",
}: LoadingSpinnerProps) => {
  return (
    <div className={`flex flex-col items-center justify-center py-10 ${className}`}>
      <div
        className="w-[40px] h-[40px] border-[4px] border-[#E5E7EB]
          border-t-[var(--color-primary)] rounded-full animate-spin mb-[12px]"
      />
      <span className="text-[14px] font-[500] text-[var(--color-text)]">{text}</span>
    </div>
  );
};
