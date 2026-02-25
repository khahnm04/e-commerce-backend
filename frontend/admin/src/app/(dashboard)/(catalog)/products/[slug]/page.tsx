"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import ProductForm from "../_components/ProductForm";
import { productService } from "@/services/catalog/product.service";
import { ProductResponse } from "@/types/catalog/product.types";
import { use } from "react";

export default function ProductEditPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = use(params);
  const [data, setData] = useState<ProductResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await productService.getById(slug);
        setData(res.data);
      } catch {
        console.error("Không thể tải dữ liệu sản phẩm");
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
          Không tìm thấy sản phẩm!
        </h2>
        <p className="text-gray-500">
          Sản phẩm này có thể đã bị xóa hoặc đường dẫn không đúng.
        </p>
      </div>
    );
  }

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