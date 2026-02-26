"use client";
import { ReactNode } from "react";

interface DataTableProps {
  headers: string[];
  children: ReactNode;
  isSelectAll?: boolean;
  onSelectAll?: () => void;
}

export const DataTable = ({ headers, children, isSelectAll, onSelectAll }: DataTableProps) => {
  return (
    <div
      className="overflow-x-auto border border-[#B9B9B9] rounded-[14px]
        overflow-hidden mb-[15px]"
    >
      <table className="bg-[#fff] w-full min-w-[1141px]">
        <thead>
          <tr>
            <th
              className="bg-[#FCFDFD] border-b border-[#D5D5D5] p-[15px]
                font-[800] text-[14px] text-[var(--color-text)] text-left w-[50px]"
            >
              <input
                className="w-[20px] h-[20px] cursor-pointer"
                type="checkbox"
                checked={isSelectAll}
                onChange={onSelectAll}
              />
            </th>
            {headers.map((item, index) => (
              <th
                key={index}
                className="bg-[#FCFDFD] border-b border-[#D5D5D5] p-[15px]
                  font-[800] text-[14px] text-[var(--color-text)] text-center"
              >
                {item}
              </th>
            ))}
          </tr>
        </thead>

        <tbody>{children}</tbody>
      </table>
    </div>
  );
};
