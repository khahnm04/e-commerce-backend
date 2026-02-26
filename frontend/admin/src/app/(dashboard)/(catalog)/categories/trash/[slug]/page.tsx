"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import CategoryForm from "../../_components/CategoryForm";

export default function CategoryTrashDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<CategoryResponse>({
    fetchFn: categoryService.getDeletedById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data)
    return (
      <NotFoundMessage
        title="Không tìm thấy danh mục!"
        message="Danh mục này không tồn tại trong thùng rác hoặc đường dẫn không đúng."
      />
    );

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chi tiết danh mục (Thùng rác)
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Danh mục", href: "/categories" },
          { label: "Thùng rác", href: "/categories/trash" },
          { label: data.name || "Chi tiết" },
        ]}
      />

      <CategoryForm
        initialData={data}
        isReadOnly={true}
      />
    </main>
  );
}
