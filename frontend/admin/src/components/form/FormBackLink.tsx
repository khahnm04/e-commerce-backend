import Link from "next/link";
import React from "react";

interface FormBackLinkProps {
  href: string;
  children?: React.ReactNode; // Nội dung text (mặc định hoặc tùy chỉnh)
  className?: string;
}

export const FormBackLink = ({ href, children, className = "" }: FormBackLinkProps) => {
  return (
    <div className={`inner-back text-center mt-[30px] ${className}`}>
      <Link
        href={href}
        className="font-[700] text-[18px] text-[var(--color-primary)]
          underline hover:no-underline transition-all cursor-pointer"
      >
        {children || "Quay lại danh sách"} {/* Nếu không truyền text thì lấy mặc định */}
      </Link>
    </div>
  );
};
