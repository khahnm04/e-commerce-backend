"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { productVariantService } from "@/services/catalog/productVariant.service";
import { ProductVariantResponse } from "@/types/catalog/product-variant.types";
import ProductVariantForm from "../_components/ProductVariantForm";

export default function ProductVariantEditPage({
  params
}: {
  params: Promise<{ slug: string }>
}) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<ProductVariantResponse>({
    fetchFn: productVariantService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy biến thể sản phẩm!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa biến thể sản phẩm
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Biến thể sản phẩm", href: "/product-variants" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />

      <ProductVariantForm initialData={data} />
    </main>
  );
}
