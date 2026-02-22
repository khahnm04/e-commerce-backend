import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CategoryForm from "../_components/CategoryForm";
import { MOCK_CATEGORY_LIST } from "@/data/mock-category";

export const metadata: Metadata = {
  title: "Chi tiết danh mục",
  description: "Trang Chi tiết danh mục sản phẩm",
};

export default async function CategoryPage({
  params,
}: {
  params: Promise<{ slug: string }>
}) {
  const { slug } = await params;

  const categoryData = MOCK_CATEGORY_LIST.find((item: any) => item.slug === slug);
  const name: string = categoryData?.name.toLowerCase() || "";

  if (!categoryData) {
    return <div className="p-10 text-center">Không tìm thấy danh mục!</div>;
  }

  return (
    <>
      <h1 className="mt-0 mb-[5px] font-bold text-[32px] text-[var(--color-text)]">
        Danh mục {name || "Chi tiết danh mục"}
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Danh mục", href: "/categories" },
          { label: categoryData.name || "Chi tiết" }
        ]}
      />

      <CategoryForm
        initialData={categoryData}
      />
    </>
  );
}
