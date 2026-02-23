import { Metadata } from "next";
import CustomerList from "../_components/CustomerList";
import { Pagination } from "@/components/common/Pagination";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Khách hàng",
  description: "Danh sách khách hàng đã xóa.",
};

export default function CustomerTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác - Khách hàng
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Khách hàng", href: "/customers" },
          { label: "Thùng rác" }
        ]}
      />

      <CustomerList isTrash={true} />

      <Pagination />
    </main>
  );
}
