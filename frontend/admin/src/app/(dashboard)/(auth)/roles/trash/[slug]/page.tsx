"use client";
import { useParams } from "next/navigation";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";
import RoleForm from "../../_components/RoleForm";

export default function ViewTrashRolePage() {
  const params = useParams();
  const slug = params.slug as string;

  const { data, loading } = useDetailPage<RoleResponse>({
    fetchFn: roleService.getDeletedById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data)
    return (
      <NotFoundMessage
        title="Không tìm thấy nhóm quyền!"
        message="Nhóm quyền này có thể đã bị xóa vĩnh viễn hoặc đường dẫn không đúng."
      />
    );

  return (
    <>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Xem chi tiết nhóm quyền đã xóa
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhóm quyền", href: "/roles" },
          { label: "Thùng rác", href: "/roles/trash" },
          { label: data.name || "Chi tiết" },
        ]}
      />
      <RoleForm initialData={data} isReadOnly={true} />
    </>
  );
}
