"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { productService } from "@/services/catalog/product.service";
import { ProductResponse } from "@/types/catalog/product.types";
import ProductForm from "../../_components/ProductForm";

export default function ProductTrashDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<ProductResponse>({
    fetchFn: productService.getDeletedById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data)
    return (
      <NotFoundMessage
        title="Không tìm thấy sản phẩm!"
        message="Sản phẩm này không tồn tại trong thùng rác hoặc đường dẫn không đúng."
      />
    );

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chi tiết sản phẩm (Thùng rác)
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm", href: "/products" },
          { label: "Thùng rác", href: "/products/trash" },
          { label: data.name || "Chi tiết" },
        ]}
      />
      <ProductForm initialData={data} isReadOnly={true} />
    </main>
  );
}
