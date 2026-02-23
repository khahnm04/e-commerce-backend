import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import VariantForm from "../_components/VariantForm";

export const metadata: Metadata = {
  title: "Thêm mới biến thể",
};

export default function VariantCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới biến thể
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Biến thể", href: "/variants" },
          { label: "Thêm mới" },
        ]}
      />

      <VariantForm />
    </main>
  );
}
