"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { employeeService } from "@/services/user/employee.service";
import { EmployeeResponse } from "@/types/user/employee.types";
import EmployeeForm from "../../_components/EmployeeForm";

export default function EmployeeTrashDetailPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<EmployeeResponse>({
    fetchFn: employeeService.getDeletedById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data)
    return (
      <NotFoundMessage
        title="Không tìm thấy nhân viên!"
        message="Nhân viên này không tồn tại trong thùng rác hoặc đường dẫn không đúng."
      />
    );

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chi tiết nhân viên (Thùng rác)
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhân viên", href: "/employees" },
          { label: "Thùng rác", href: "/employees/trash" },
          { label: data.fullName || "Chi tiết" },
        ]}
      />
      <EmployeeForm initialData={data} isReadOnly={true} />
    </main>
  );
}
