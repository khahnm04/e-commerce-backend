import Link from "next/link";
import React from "react";

interface BreadcrumbItem {
  label: string;
  href?: string;
}

interface BreadcrumbProps {
  items: BreadcrumbItem[];
  className?: string;
}

export const Breadcrumb = ({ items, className = "" }: BreadcrumbProps) => {
  return (
    <nav className={`flex items-center text-[14px] ${className}`}>
      {items.map((item, index) => {
        const isLast = index === items.length - 1;

        return (
          <div key={index} className="flex items-center">
            {!isLast && item.href ? (
              <Link
                href={item.href}
                className="text-[#606060] hover:text-[var(--color-primary)] transition-colors cursor-pointer"
              >
                {item.label}
              </Link>
            ) : (
              <span className="font-[600] text-[var(--color-text)]">{item.label}</span>
            )}

            {!isLast && <span className="mx-2 text-[#999999]">/</span>}
          </div>
        );
      })}
    </nav>
  );
};
