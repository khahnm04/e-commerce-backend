import React from "react";
import { useFilePond } from "@/hooks/useFilePond";

interface FormImageUploadProps
  extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
}

export const FormImageUpload = ({
  label,
  id,
  className = "",
  ...props
}: FormImageUploadProps) => {
  useFilePond();

  return (
    <div
      className={`inner-group relative inner-two-col md:col-span-2 col-span-1 ${className}`}
    >
      <label
        htmlFor={id}
        className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
      >
        {label}
      </label>

      <input
        id={id}
        type="file"
        className="filepond"
        {...props}
      />
    </div>
  );
};
