import React, { forwardRef, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa6";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
}

export const FormInput = forwardRef<HTMLInputElement, FormInputProps>(
  ({ label, id, error, className = "", type, ...props }, ref) => {
    const [showPassword, setShowPassword] = useState(false);
    const isPasswordType = type === "password";

    return (
      <div className={`inner-group relative ${className}`}>
        <label
          className="inner-label block font-[600] text-[14px]
            text-[#606060] mb-[11px]"
          htmlFor={id}
        >
          {label}
        </label>

        <div className="relative">
          <input
            ref={ref}
            id={id}
            type={isPasswordType && showPassword ? "text" : type}
            className={`
              bg-[#F5F6FA] border rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] focus:outline-none 
              ${isPasswordType ? "pr-[50px]" : ""}
              ${
                error
                  ? "border-red-500 focus:border-red-500"
                  : "border-[#D5D5D5] focus:border-[var(--color-primary)]"
              }
            `}
            {...props}
          />

          {isPasswordType && (
            <button
              type="button"
              className="absolute right-[16px] top-1/2 -translate-y-1/2
                text-[18px] text-[#808080] hover:text-[#333] transition-colors
                bg-transparent border-0 cursor-pointer p-0"
              onClick={() => setShowPassword(!showPassword)}
              tabIndex={-1}
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </button>
          )}
        </div>

        {error && (
          <span className="text-red-500 text-[13px] mt-[6px] block font-medium">{error}</span>
        )}
      </div>
    );
  },
);

FormInput.displayName = "FormInput";
