import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CustomerForm from "../_components/CustomerForm";

export const metadata: Metadata = {
  title: "Thêm mới khách hàng",
};

export default function CustomerCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới khách hàng
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Khách hàng", href: "/customers" },
          { label: "Thêm mới" },
        ]}
      />

      <CustomerForm />
    </main>
  );
}
