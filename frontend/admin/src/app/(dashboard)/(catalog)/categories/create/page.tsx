import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CategoryForm from "../_components/CategoryForm";

export const metadata: Metadata = {
  title: "Tạo mới danh mục",
  description: "Trang thêm mới danh mục sản phẩm",
};

export default function CategoryPage() {
  return (
    <>
      <h1 className="mt-0 mb-[5px] font-bold text-[32px] text-[var(--color-text)]">Tạo danh mục</h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Danh mục", href: "/categories" },
          { label: "Tạo mới" },
        ]}
      />

      <CategoryForm />
    </>
  );
}
