"use client";
import { ReactNode, Children } from "react";

interface DataTableProps {
  headers: string[];
  children: ReactNode;
  isSelectAll?: boolean;
  onSelectAll?: () => void;
  emptyMessage?: string;
}

export const DataTable = ({
  headers,
  children,
  isSelectAll,
  onSelectAll,
  emptyMessage = "Không có dữ liệu",
}: DataTableProps) => {
  const childCount = Children.count(children);

  return (
    <div
      className="overflow-x-auto border border-[#B9B9B9] rounded-[14px]
        overflow-hidden mb-[15px]"
    >
      <table className="bg-[#fff] w-full min-w-[1141px]" role="table">
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
                aria-label="Chọn tất cả"
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

        <tbody>
          {childCount === 0 ? (
            <tr>
              <td
                colSpan={headers.length + 1}
                className="text-center py-[40px] text-gray-400 text-[14px] italic"
              >
                {emptyMessage}
              </td>
            </tr>
          ) : (
            children
          )}
        </tbody>
      </table>
    </div>
  );
};
