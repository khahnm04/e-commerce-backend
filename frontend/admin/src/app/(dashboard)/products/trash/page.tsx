import { Metadata } from "next";
import { Pagination } from "@/components/common/Pagination";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import ProductList from "../_components/ProductList";

export const metadata: Metadata = {
  title: "Thùng rác sản phẩm",
  description: "Danh sách sản phẩm đã bị xóa tạm thời.",
};

export default function ProductTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác sản phẩm
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm", href: "/products" },
          { label: "Thùng rác" }
        ]}
      />

      <ProductList isTrash={true} />

      <Pagination />
    </main>
  );
}
