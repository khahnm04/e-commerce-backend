import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import VariantValueForm from "../_components/VariantValueForm";

export const metadata: Metadata = {
  title: "Thêm mới giá trị biến thể",
};

export default function VariantValueCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới giá trị biến thể
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Giá trị biến thể", href: "/variant-values" },
          { label: "Thêm mới" },
        ]}
      />

      <VariantValueForm />
    </main>
  );
}
