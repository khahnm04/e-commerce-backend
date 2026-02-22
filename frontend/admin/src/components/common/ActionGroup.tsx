"use client";
import Link from "next/link";
import { FaRegPenToSquare, FaRegTrashCan } from "react-icons/fa6";
import { FaTrashRestore } from "react-icons/fa";

interface ActionGroupProps {
  isTrash?: boolean;
  editLink?: string;
  onDelete: () => void;
  onRestore?: () => void;
}

export const ActionGroup = ({
  isTrash = false,
  editLink = "#",
  onDelete,
  onRestore
}: ActionGroupProps) => {
  return (
    <>
      <div className="bg-[#FAFBFD] border border-[#D5D5D5] inline-flex rounded-[8px]">
        {isTrash ? (
          <>
            <button
              className="inline-block px-[16.5px] py-[8.5px] border-r border-[#D5D5D5] text-[15px] text-[#10B981] hover:bg-green-50 cursor-pointer transition-colors"
              title="Khôi phục"
              onClick={onRestore}
            >
              <FaTrashRestore />
            </button>

            <button
              className="inline-block px-[16.5px] py-[8.5px] text-[15px] text-[#EF3826] hover:bg-red-50 cursor-pointer transition-colors"
              title="Xóa vĩnh viễn"
              onClick={onDelete}
            >
              <FaRegTrashCan />
            </button>
          </>
        ) : (
          <>
            <Link
              className="inline-block px-[16.5px] py-[8.5px] border-r border-[#D5D5D5] text-[15px] text-[rgba(0, 0, 0, 0.6)] hover:bg-gray-50 transition-colors"
              href={editLink}
              title="Chỉnh sửa"
            >
              <FaRegPenToSquare />
            </Link>

            <button
              className="inline-block px-[16.5px] py-[8.5px] text-[15px] text-[#EF3826] hover:bg-red-50 cursor-pointer transition-colors"
              title="Xóa vào thùng rác"
              onClick={onDelete}
            >
              <FaRegTrashCan />
            </button>
          </>
        )}
      </div>
    </>
  );
};
