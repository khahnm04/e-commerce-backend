import React from "react";

interface NotFoundMessageProps {
  title?: string;
  message?: string;
}

/**
 * Shared "not found" message component for detail pages.
 * Replaces the duplicated error state JSX across all edit/view pages.
 */
export const NotFoundMessage = ({
  title = "Không tìm thấy dữ liệu!",
  message = "Dữ liệu này có thể đã bị xóa hoặc đường dẫn không đúng.",
}: NotFoundMessageProps) => {
  return (
    <div className="p-10 text-center">
      <h2 className="text-xl font-bold text-red-500 mb-2">{title}</h2>
      <p className="text-gray-500">{message}</p>
    </div>
  );
};
