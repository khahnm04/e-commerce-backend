"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { attributeService } from "@/services/catalog/attribute.service";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import AttributeForm from "../_components/AttributeForm";

export default function AttributeEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<AttributeResponse>({
    fetchFn: attributeService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy thuộc tính!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa thuộc tính
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Thuộc tính", href: "/attributes" },
          { label: data.name || "Chỉnh sửa" },
        ]}
      />
      <AttributeForm initialData={data} />
    </main>
  );
}
