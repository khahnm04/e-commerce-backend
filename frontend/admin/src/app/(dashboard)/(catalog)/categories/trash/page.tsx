import { Metadata } from "next";
import { Pagination } from "@/components/common/Pagination";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CategoryList from "../_components/CategoryList";

export const metadata: Metadata = {
  title: "Quản lý danh mục",
  description: "Danh sách danh mục sản phẩm, bộ lọc và tìm kiếm.",
};

export default function CategoryPage() {
  return (
    <>
      <main className="">
        <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
          Thùng rác danh mục
        </h1>

        <Breadcrumb
          className="mb-[30px] ml-[5px]"
          items={[
            { label: "Tổng quan", href: "/" },
            { label: "Danh mục", href: "/categories" },
            { label: "Thùng rác" }
          ]}
        />

        <CategoryList
          isTrash={true}
        />

        <Pagination />
      </main>
    </>
  )
}
