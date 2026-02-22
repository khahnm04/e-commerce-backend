import React, { forwardRef } from "react";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
}

export const FormInput = forwardRef<HTMLInputElement, FormInputProps>(
  ({ label, id, error, className = "", ...props }, ref) => {
    return (
      <div className={`inner-group relative ${className}`}>
        <label
          className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
          htmlFor={id}
        >
          {label}
        </label>

        <input
          ref={ref}
          id={id}
          className={`
            bg-[#F5F6FA] border rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] focus:outline-none 
            ${error
              ? "border-red-500 focus:border-red-500"
              : "border-[#D5D5D5] focus:border-[var(--color-primary)]"
            }
          `}
          {...props}
        />

        {error && (
          <span className="text-red-500 text-[13px] mt-[6px] block font-medium">
            {error}
          </span>
        )}
      </div>
    );
  }
);

FormInput.displayName = "FormInput";
