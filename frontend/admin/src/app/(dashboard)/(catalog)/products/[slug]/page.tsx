"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { productService } from "@/services/catalog/product.service";
import { ProductResponse } from "@/types/catalog/product.types";
import ProductForm from "../_components/ProductForm";

export default function ProductEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<ProductResponse>({
    fetchFn: productService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy sản phẩm!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa sản phẩm
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm", href: "/products" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />
      <ProductForm initialData={data} />
    </main>
  );
}
