"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import VariantValueForm from "../_components/VariantValueForm";
import { variantValueService } from "@/services/variantValue.service";
import { VariantValueResponse } from "@/types/variant-value.types";
import { use } from "react";

export default function VariantValueEditPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = use(params);
  const [data, setData] = useState<VariantValueResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await variantValueService.getById(slug);
        setData(res.data);
      } catch {
        console.error("Không thể tải dữ liệu giá trị biến thể");
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
          Không tìm thấy giá trị biến thể!
        </h2>
        <p className="text-gray-500">
          Giá trị biến thể này có thể đã bị xóa hoặc đường dẫn không đúng.
        </p>
      </div>
    );
  }

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
