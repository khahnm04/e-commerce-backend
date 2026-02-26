import { Metadata } from "next";
import ProductVariantList from "./_components/ProductVariantList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý biến thể sản phẩm",
  description: "Quản lý biến thể sản phẩm",
};

export default function ProductVariantPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý biến thể sản phẩm
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[{ label: "Tổng quan", href: "/" }, { label: "Biến thể sản phẩm" }]}
      />
      <ProductVariantList />
    </main>
  );
}
