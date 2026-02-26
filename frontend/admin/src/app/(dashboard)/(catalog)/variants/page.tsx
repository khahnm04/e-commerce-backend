import { Metadata } from "next";
import VariantList from "./_components/VariantList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý biến thể",
  description: "Quản lý biến thể",
};

export default function VariantPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý biến thể
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[{ label: "Tổng quan", href: "/" }, { label: "Biến thể" }]}
      />
      <VariantList />
    </main>
  );
}
