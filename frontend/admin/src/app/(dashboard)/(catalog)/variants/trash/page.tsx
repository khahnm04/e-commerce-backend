import { Metadata } from "next";
import VariantList from "../_components/VariantList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Biến thể",
  description: "Danh sách Biến thể đã xóa.",
};

export default function VariantTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">Thùng rác - Biến thể</h1>
      <Breadcrumb className="mb-[30px] ml-[5px]" items={[{ label: "Tổng quan", href: "/" }, { label: "Biến thể", href: "/variants" }, { label: "Thùng rác" }]} />
      <VariantList isTrash={true} />
    </main>
  );
}
