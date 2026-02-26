"use client";
/* eslint-disable @next/next/no-img-element */
import Link from "next/link";
import { DashboardShell } from "@/components/layout/DashboardShell";

export default function NotFound() {
  return (
    <DashboardShell>
      <div
        className="border border-[#B9B9B9] bg-white rounded-[14px] text-center
          p-[40px_30px] md:p-[80px]"
      >
        <img
          src="/assets/images/error-404.svg"
          alt="Error 404"
          className="mx-auto h-auto w-[60%] mb-[30px] md:w-[390px] md:mb-[60px]"
        />

        <h1
          className="font-bold text-[var(--color-text)] mt-0
            text-[24px] mb-[20px] md:text-[32px] md:mb-[30px]"
        >
          Trang này không tồn tại...
        </h1>

        <Link
          href="/"
          className="inline-block bg-[var(--color-primary)] rounded-[8px] opacity-90
            font-bold text-white hover:opacity-100 transition-opacity
            py-[15px] px-[40px] text-[16px] md:px-[104px] md:text-[20px]"
        >
          Trở về trang tổng quan
        </Link>
      </div>
    </DashboardShell>
  );
}
