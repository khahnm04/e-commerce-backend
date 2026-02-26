"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import CategoryForm from "../_components/CategoryForm";

export default function CategoryEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<CategoryResponse>({
    fetchFn: categoryService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy danh mục!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa danh mục
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Danh mục", href: "/categories" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />
      <CategoryForm initialData={data} />
    </main>
  );
}
