import React from "react";

interface TableCheckboxProps {
  checked?: boolean;
  onChange?: () => void;
}

export const TableCheckbox = ({ checked, onChange }: TableCheckboxProps) => {
  return (
    <>
      <input
        className="w-[20px] h-[20px] cursor-pointer"
        type="checkbox"
        checked={checked}
        onChange={onChange}
      />
    </>
  );
};
