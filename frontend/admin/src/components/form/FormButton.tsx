import React from "react";

interface FormButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

export const FormButton = ({
  children,
  className = "",
  type = "submit",
  ...props
}: FormButtonProps) => {
  return (
    <div className="inner-button inner-two-col md:col-span-2 col-span-1 text-center cursor-pointer">
      <button
        type={type}
        {...props}
        className={`
          w-[274px] h-[56px] bg-[var(--color-primary)] rounded-[12px]
          font-[700] text-[18px] text-white opacity-[0.9]
          hover:opacity-[1] transition-all cursor-pointer
          ${className}
        `}
      >
        {children}
      </button>
    </div>
  );
};
