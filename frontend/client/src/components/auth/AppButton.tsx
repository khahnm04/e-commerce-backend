"use client";
import React from "react";

export const AppButton = (props: {
  children: React.ReactNode;
  onClick?: () => void;
  disabled?: boolean;
}) => {
  const { children, onClick, disabled } = props;

  return (
    <button
      type="submit"
      onClick={onClick}
      disabled={disabled}
      className="w-full bg-[#D70018] hover:bg-[#b00014] px-[16px] py-[11px] rounded-[8px] cursor-pointer font-[500] text-[16px] text-[#fff] transition-colors disabled:opacity-60"
    >
      {children}
    </button>
  );
}