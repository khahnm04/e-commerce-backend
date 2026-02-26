"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import CustomerForm from "../../_components/CustomerForm";

export default function CustomerTrashDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<CustomerResponse>({
    fetchFn: customerService.getDeletedById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data)
    return (
      <NotFoundMessage
        title="Không tìm thấy khách hàng!"
        message="Khách hàng này không tồn tại trong thùng rác hoặc đường dẫn không đúng."
      />
    );

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chi tiết khách hàng (Thùng rác)
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Khách hàng", href: "/customers" },
          { label: "Thùng rác", href: "/customers/trash" },
          { label: data.fullName || "Chi tiết" },
        ]}
      />
      <CustomerForm initialData={data} isReadOnly={true} />
    </main>
  );
}
