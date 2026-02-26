import { Metadata } from "next";
import ProductList from "./_components/ProductList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý sản phẩm",
  description: "Danh sách sản phẩm, bộ lọc và tìm kiếm.",
};

export default function ProductPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý sản phẩm
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm" }
        ]}
      />

      <ProductList />
    </main>
  );
}
