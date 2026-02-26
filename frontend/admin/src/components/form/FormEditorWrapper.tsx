import React, { ReactNode } from "react";

interface FormEditorWrapperProps {
  label: string;
  children: ReactNode; // Nhận vào component TinyMCE từ hook
  className?: string;
}

export const FormEditorWrapper = ({ label, children, className = "" }: FormEditorWrapperProps) => {
  return (
    <>
      <div className={`inner-group relative inner-two-col md:col-span-2 col-span-1 ${className}`}>
        <label className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]">
          {label}
        </label>
        <div className="editor-wrapper">{children}</div>
      </div>
    </>
  );
};
