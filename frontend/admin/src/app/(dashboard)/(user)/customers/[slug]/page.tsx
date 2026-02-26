"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import CustomerForm from "../_components/CustomerForm";

export default function CustomerEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<CustomerResponse>({
    fetchFn: customerService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy khách hàng!" />;

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
