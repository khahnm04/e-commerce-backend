import { Metadata } from "next";
import BrandList from "../_components/BrandList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Nhãn hàng",
  description: "Danh sách Nhãn hàng đã xóa.",
};

export default function BrandTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">Thùng rác nhãn hàng</h1>
      <Breadcrumb className="mb-[30px] ml-[5px]" items={[{ label: "Tổng quan", href: "/" }, { label: "Nhãn hàng", href: "/brands" }, { label: "Thùng rác" }]} />
      <BrandList isTrash={true} />
    </main>
  );
}
