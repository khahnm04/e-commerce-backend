"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { variantService } from "@/services/catalog/variant.service";
import { VariantResponse } from "@/types/catalog/variant.types";
import VariantForm from "../_components/VariantForm";

export default function VariantEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<VariantResponse>({
    fetchFn: variantService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy biến thể!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa biến thể
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Biến thể", href: "/variants" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />
      <VariantForm initialData={data} />
    </main>
  );
}
