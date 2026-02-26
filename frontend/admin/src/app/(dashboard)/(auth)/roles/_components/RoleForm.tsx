"use client";
import React, { useEffect, useState } from "react";
import { usePathname } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { CreateRoleSchema, CreateRoleValues } from "../role.schema";
import { roleService } from "@/services/auth/role.service";
import { permissionService } from "@/services/auth/permission.service";
import { PermissionResponse, RoleResponse, RoleRequest } from "@/types/auth/role.types";
import { PermissionSelection } from "./permission-selection";
import { useFormSubmit } from "@/hooks/useFormSubmit";

interface RoleFormProps {
  initialData?: RoleResponse;
  isReadOnly?: boolean;
}

export default function RoleForm({ initialData, isReadOnly = false }: RoleFormProps) {
  const pathname = usePathname();
  const [permissions, setPermissions] = useState<PermissionResponse[]>([]);
  const [loadingPermissions, setLoadingPermissions] = useState(true);
  const isEditMode = !!initialData;
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/roles" });

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<CreateRoleValues>({
    resolver: zodResolver(CreateRoleSchema),
    defaultValues: {
      code: initialData?.code || "",
      name: initialData?.name || "",
      description: initialData?.description || "",
      permissions: [],
    },
  });

  useEffect(() => {
    const fetchPermissions = async () => {
      try {
        const res = await permissionService.getAll();
        const allPerms = res.data || [];
        setPermissions(allPerms);

        if (isEditMode && initialData) {
          const selectedIds = allPerms
            .filter((p) => (initialData.permissions as unknown as string[]).includes(p.code))
            .map((p) => p.id);
          setValue("permissions", selectedIds);
        }
      } catch (error) {
        console.error("Lỗi khi tải quyền:", error);
        toast.error("Không thể tải danh sách quyền!");
      } finally {
        setLoadingPermissions(false);
      }
    };
    fetchPermissions();
  }, [initialData, isEditMode, setValue]);

  const onSubmit = async (data: CreateRoleValues) => {
    await submitHandler(
      async () => {
        const payload: RoleRequest = {
          code: data.code,
          name: data.name,
          description: data.description,
          permissions: data.permissions,
        };

        if (isEditMode && initialData) {
          await roleService.update(initialData.id, payload);
        } else {
          await roleService.create(payload);
        }
      },
      isEditMode ? "Cập nhật nhóm quyền thành công!" : "Tạo nhóm quyền mới thành công!",
    );
  };

  const selectedPermissions = watch("permissions");

  return (
    <>
      <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
        <form
          id="role-form"
          className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
          onSubmit={handleSubmit(onSubmit)}
        >
          <FormInput
            label="Mã nhóm quyền (Code)"
            id="code"
            type="text"
            placeholder="Ví dụ: ADMIN, MANAGER, ..."
            {...register("code")}
            error={errors.code?.message}
            disabled={isReadOnly}
          />

          <FormInput
            label="Tên nhóm quyền"
            id="name"
            type="text"
            {...register("name")}
            error={errors.name?.message}
            disabled={isReadOnly}
          />

          <div className="md:col-span-2">
            <FormInput
              label="Mô tả"
              id="description"
              type="text"
              {...register("description")}
              error={errors.description?.message}
              disabled={isReadOnly}
            />
          </div>

          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Phân quyền
              <span className="text-red-500">*</span>
            </label>
            {loadingPermissions ? (
              <p>Đang tải danh sách quyền...</p>
            ) : (
              <>
                {errors.permissions && (
                  <p className="text-red-500 text-sm mb-3">
                    {errors.permissions.message}
                  </p>
                )}
                <PermissionSelection
                  permissions={permissions}
                  selectedPermissions={selectedPermissions || []}
                  onChange={(ids) => setValue("permissions", ids)}
                  isReadOnly={isReadOnly}
                />
              </>
            )}
          </div>

          {!isReadOnly && (
            <FormButton
              type="submit"
              disabled={isLoading}
            >
              {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
            </FormButton>
          )}
        </form>

        <FormBackLink href={pathname.includes("/trash") ? "/roles/trash" : "/roles"} />
      </div>
    </>
  );
}
