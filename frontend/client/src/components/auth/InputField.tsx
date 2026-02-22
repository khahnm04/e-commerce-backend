"use client";
import { forwardRef, useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa6";

type InputFieldProps = {
  label: string;
  type: string;
  id: string;
  placeholder: string;
  icon?: string;
  error?: string;
} & React.InputHTMLAttributes<HTMLInputElement>;

export const InputField = forwardRef<HTMLInputElement, InputFieldProps>(
  ({ label, type, id, placeholder, icon, max, min, error, ...rest }, ref) => {
    const [showPassword, setShowPassword] = useState(false);

    // Xác định type thực tế của input
    const inputType = type === "password" && showPassword ? "text" : type;

    // Toggle hiển thị mật khẩu
    const togglePasswordVisibility = () => {
      setShowPassword(!showPassword);
    };

    return (
      <div className="flex flex-col gap-2 relative w-full">
        <label
          className="font-medium text-sm text-[#121214] cursor-pointer"
          htmlFor={id}
        >
          {label}
        </label>

        <div className={`group border rounded-lg flex items-center px-4 py-3 transition-all duration-200 bg-white ${error ? 'border-[#e63946]' : 'border-[#CFCFD3] focus-within:border-[#374151] focus-within:ring-1 focus-within:ring-[#374151] hover:border-[#374151]'}`}>
          <input
            ref={ref}
            type={inputType}
            id={id}
            name={id}
            placeholder={placeholder}
            max={max}
            min={min}
            className="flex-1 w-full text-base outline-none border-none bg-transparent text-[#121214] placeholder:font-normal placeholder:text-[#CFCFD3]"
            {...rest}
          />

          {type === "password" ? (
            <button
              type="button"
              onClick={togglePasswordVisibility}
              className="ml-2 cursor-pointer opacity-60 hover:opacity-100 transition-opacity"
            >
              {showPassword ? (
                <FaEyeSlash className="w-5 h-5" />
              ) : (
                <FaEye className="w-5 h-5" />
              )}
            </button>
          ) : (
            icon && (
              <img
                src={icon}
                className="w-5 h-5 ml-2 cursor-pointer opacity-60 hover:opacity-100 transition-opacity"
                alt="icon"
              />
            )
          )}
        </div>

        {error && (
          <p className="absolute top-[104%] left-0 w-full text-[12px] text-[#e63946]">
            {error}
          </p>
        )}
      </div>
    );
  }
);

InputField.displayName = "InputField";