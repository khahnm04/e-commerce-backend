import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import BrandForm from "../_components/BrandForm";

export const metadata: Metadata = {
  title: "Thêm mới nhãn hàng",
};

export default function BrandCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới nhãn hàng
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhãn hàng", href: "/brands" },
          { label: "Thêm mới" },
        ]}
      />

      <BrandForm />
    </main>
  );
}
