"use client";
import { useParams } from "next/navigation";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { NotFoundMessage } from "@/components/common/NotFoundMessage";
import { useDetailPage } from "@/hooks/useDetailPage";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";
import RoleForm from "../_components/RoleForm";

export default function EditRolePage() {
  const params = useParams();
  const slug = params.slug as string;

  const { data, loading } = useDetailPage<RoleResponse>({
    fetchFn: roleService.getById,
    id: slug,
  });

  if (loading) return <LoadingSpinner />;
  if (!data) return <NotFoundMessage title="Không tìm thấy nhóm quyền!" />;

  return (
    <>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Cập nhật nhóm quyền
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhóm quyền", href: "/roles" },
          { label: data.name || "Cập nhật" },
        ]}
      />
      <RoleForm initialData={data} />
    </>
  );
}
