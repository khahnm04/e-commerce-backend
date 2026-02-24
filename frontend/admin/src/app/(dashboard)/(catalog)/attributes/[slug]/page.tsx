"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import AttributeForm from "../_components/AttributeForm";
import { attributeService } from "@/services/catalog/attribute.service";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import { use } from "react";

export default function AttributeEditPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = use(params);
  const [data, setData] = useState<AttributeResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await attributeService.getById(slug);
        setData(res.data);
      } catch {
        console.error("Không thể tải dữ liệu thuộc tính");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [slug]);

  if (loading) {
    return (
      <div className="text-center py-10 text-[var(--color-text)]">
        Đang tải dữ liệu...
      </div>
    );
  }

  if (!data) {
    return (
      <div className="p-10 text-center">
        <h2 className="text-xl font-bold text-red-500 mb-2">
          Không tìm thấy thuộc tính!
        </h2>
        <p className="text-gray-500">
          Thuộc tính này có thể đã bị xóa hoặc đường dẫn không đúng.
        </p>
      </div>
    );
  }

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
