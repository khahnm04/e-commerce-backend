"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CustomerForm from "../_components/CustomerForm";
import { customerService } from "@/services/customer.service";
import { CustomerResponse } from "@/types/customer.types";
import { use } from "react";

export default function CustomerEditPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = use(params);
  const [data, setData] = useState<CustomerResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await customerService.getById(slug);
        setData(res.data);
      } catch {
        console.error("Không thể tải dữ liệu khách hàng");
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
          Không tìm thấy khách hàng!
        </h2>
        <p className="text-gray-500">
          Khách hàng này có thể đã bị xóa hoặc đường dẫn không đúng.
        </p>
      </div>
    );
  }

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa khách hàng
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Khách hàng", href: "/customers" },
          { label: data.fullName || "Chỉnh sửa" },
        ]}
      />

      <CustomerForm initialData={data} />
    </main>
  );
}
