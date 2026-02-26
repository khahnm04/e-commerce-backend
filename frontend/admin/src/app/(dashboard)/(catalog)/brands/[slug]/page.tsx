"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { brandService } from "@/services/catalog/brand.service";
import { BrandResponse } from "@/types/catalog/brand.types";
import BrandForm from "../_components/BrandForm";

export default function BrandEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<BrandResponse>({
    fetchFn: brandService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy nhãn hàng!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa nhãn hàng
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhãn hàng", href: "/brands" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />
      <BrandForm initialData={data} />
    </main>
  );
}
