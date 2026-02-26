"use client";
import Link from "next/link";
import { FaRegPenToSquare, FaRegTrashCan, FaRegEye } from "react-icons/fa6";
import { FaTrashRestore } from "react-icons/fa";

interface ActionGroupProps {
  isTrash?: boolean;
  editLink?: string;
  viewLink?: string;
  onDelete?: () => void;
  onRestore?: () => void;
  hideDelete?: boolean;
}

export const ActionGroup = ({
  isTrash = false,
  editLink = "#",
  viewLink,
  onDelete,
  onRestore,
  hideDelete = false,
}: ActionGroupProps) => {
  return (
    <>
      <div
        className="bg-[#FAFBFD] border border-[#D5D5D5] inline-flex
          rounded-[8px] overflow-hidden"
      >
        {isTrash ? (
          <>
            {viewLink && (
              <Link
                className="inline-block px-[16.5px] py-[8.5px]
                  border-r border-[#D5D5D5] text-[15px] text-[#3B82F6]
                  hover:bg-blue-50 cursor-pointer transition-colors"
                href={viewLink}
                title="Xem chi tiết"
              >
                <FaRegEye />
              </Link>
            )}

            {onRestore && (
              <button
                className={`inline-block px-[16.5px] py-[8.5px]
                  ${!hideDelete ? "border-r border-[#D5D5D5]" : ""}
                  text-[15px] text-[#10B981] hover:bg-green-50
                  cursor-pointer transition-colors`}
                title="Khôi phục"
                onClick={onRestore}
              >
                <FaTrashRestore />
              </button>
            )}

            {!hideDelete && onDelete && (
              <button
                className="inline-block px-[16.5px] py-[8.5px] text-[15px]
                  text-[#EF3826] hover:bg-red-50 cursor-pointer
                  transition-colors"
                title="Xóa vĩnh viễn"
                onClick={onDelete}
              >
                <FaRegTrashCan />
              </button>
            )}
          </>
        ) : (
          <>
            <Link
              className="inline-block px-[16.5px] py-[8.5px]
                border-r border-[#D5D5D5] text-[15px]
                text-[rgba(0,_0,_0,_0.6)] hover:bg-gray-50
                cursor-pointer transition-colors"
              href={editLink}
              title="Chỉnh sửa"
            >
              <FaRegPenToSquare />
            </Link>

            {!hideDelete && onDelete && (
              <button
                className="inline-block px-[16.5px] py-[8.5px] text-[15px]
                  text-[#EF3826] hover:bg-red-50 cursor-pointer
                  transition-colors"
                title="Xóa vào thùng rác"
                onClick={onDelete}
              >
                <FaRegTrashCan />
              </button>
            )}
          </>
        )}
      </div>
    </>
  );
};
