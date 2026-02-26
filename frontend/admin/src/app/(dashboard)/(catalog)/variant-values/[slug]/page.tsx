"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { variantValueService } from "@/services/catalog/variantValue.service";
import { VariantValueResponse } from "@/types/catalog/variant-value.types";
import VariantValueForm from "../_components/VariantValueForm";

export default function VariantValueEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<VariantValueResponse>({
    fetchFn: variantValueService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy giá trị biến thể!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa giá trị biến thể
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Giá trị biến thể", href: "/variant-values" },
          { label: data.value || "Chỉnh sửa" },
        ]}
      />
      <VariantValueForm initialData={data} />
    </main>
  );
}
