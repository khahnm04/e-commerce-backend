import { Metadata } from "next";
import BrandList from "./_components/BrandList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý nhãn hàng",
  description: "Quản lý nhãn hàng",
};

export default function BrandPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý nhãn hàng
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[{ label: "Tổng quan", href: "/" }, { label: "Nhãn hàng" }]}
      />
      <BrandList />
    </main>
  );
}
