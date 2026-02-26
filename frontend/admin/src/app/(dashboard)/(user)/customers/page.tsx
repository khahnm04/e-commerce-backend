import { Metadata } from "next";
import CustomerList from "./_components/CustomerList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý khách hàng",
  description: "Danh sách khách hàng, bộ lọc và tìm kiếm.",
};

export default function CustomerPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý khách hàng
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[{ label: "Tổng quan", href: "/" }, { label: "Khách hàng" }]}
      />

      <CustomerList />
    </main>
  );
}
