"use client";
import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import RoleForm from "../_components/RoleForm";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";

export default function EditRolePage() {
  const params = useParams();
  const slug = params.slug as string;

  const [initialData, setInitialData] = useState<RoleResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchRole = async () => {
      try {
        const res = await roleService.getById(slug);
        setInitialData(res.data);
      } catch (err) {
        console.error("Lỗi khi tải nhóm quyền:", err);
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    fetchRole();
  }, [slug]);

  if (loading) {
    return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
  }

  if (error || !initialData) {
    return (
      <div className="p-10 text-center">
        <h2 className="text-xl font-bold text-red-500 mb-2">
          Không tìm thấy nhóm quyền!
        </h2>
        <p className="text-gray-500">
          Nhóm quyền này có thể đã bị xóa hoặc đường dẫn không đúng.
        </p>
      </div>
    );
  }

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
          { label: initialData.name || "Cập nhật" }
        ]}
      />

      <RoleForm initialData={initialData} />
    </>
  )
}
