import React from "react";

interface TableCellProps {
  children: React.ReactNode;
  className?: string;
}

export const TableCell = ({ children, className = "" }: TableCellProps) => {
  return (
    <>
      <td
        className={`py-[8px] px-[15px] border-b
          border-[rgba(151,151,151,0.6)] text-[14px] font-[600]
          text-[var(--color-text)]
          ${className === "" ? "text-center" : className}`}
      >
        {children}
      </td>
    </>
  );
};
