import { Metadata } from "next";
import CategoryList from "../_components/CategoryList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Danh mục",
  description: "Danh sách danh mục đã xóa.",
};

export default function CategoryTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác danh mục
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Danh mục", href: "/categories" },
          { label: "Thùng rác" },
        ]}
      />

      <CategoryList isTrash={true} />
    </main>
  );
}
