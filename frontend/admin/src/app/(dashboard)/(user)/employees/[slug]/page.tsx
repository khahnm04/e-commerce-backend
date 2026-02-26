"use client";
import { use } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { employeeService } from "@/services/user/employee.service";
import { EmployeeResponse } from "@/types/user/employee.types";
import EmployeeForm from "../_components/EmployeeForm";

export default function EmployeeEditPage({ params }: { params: Promise<{ slug: string }> }) {
  const { slug } = use(params);
  const { data, loading } = useDetailPage<EmployeeResponse>({
    fetchFn: employeeService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy nhân viên!" />;

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Chỉnh sửa nhân viên
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhân viên", href: "/employees" },
          { label: data.fullName || "Chỉnh sửa" },
        ]}
      />
      <EmployeeForm initialData={data} />
    </main>
  );
}
