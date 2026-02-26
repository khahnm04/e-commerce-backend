import React from "react";

interface Option {
  value: string | number;
  label: string;
}

interface FormSelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  label: string;
  id: string;
  options: Option[];
}

export const FormSelect = ({ label, id, options, className = "", ...props }: FormSelectProps) => {
  return (
    <>
      <div className={`inner-group relative ${className}`}>
        <label
          className="inner-label block font-[600] text-[14px]
            text-[#606060] mb-[11px]"
          htmlFor={id}
        >
          {label}
        </label>
        <select
          id={id}
          className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
            px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full
            h-[52px] focus:outline-none focus:border-[var(--color-primary)]
            cursor-pointer"
          {...props}
        >
          {options.map((opt, index) => (
            <option key={index} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      </div>
    </>
  );
};
