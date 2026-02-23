import { Metadata } from "next";
import ProductVariantList from "../_components/ProductVariantList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Biến thể sản phẩm",
  description: "Danh sách Biến thể sản phẩm đã xóa.",
};

export default function ProductVariantTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">Thùng rác - Biến thể sản phẩm</h1>
      <Breadcrumb className="mb-[30px] ml-[5px]" items={[{ label: "Tổng quan", href: "/" }, { label: "Biến thể sản phẩm", href: "/product-variants" }, { label: "Thùng rác" }]} />
      <ProductVariantList isTrash={true} />
    </main>
  );
}
