import { Metadata } from "next";
import VariantValueList from "./_components/VariantValueList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý giá trị biến thể",
  description: "Quản lý giá trị biến thể",
};

export default function VariantValuePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">Quản lý giá trị biến thể</h1>
      <Breadcrumb className="mb-[30px] ml-[5px]" items={[{ label: "Tổng quan", href: "/" }, { label: "Giá trị biến thể" }]} />
      <VariantValueList />
    </main>
  );
}
