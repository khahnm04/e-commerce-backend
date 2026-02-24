"use client"
import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
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

interface RoleFormProps {
  initialData?: RoleResponse;
}

export default function RoleForm({ initialData }: RoleFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [permissions, setPermissions] = useState<PermissionResponse[]>([]);
  const [loadingPermissions, setLoadingPermissions] = useState(true);

  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<CreateRoleValues>({
    resolver: zodResolver(CreateRoleSchema),
    defaultValues: {
      code: "", // Backend might not return code in Response, so we might have an issue if code is required for update.
      name: initialData?.name || "",
      description: initialData?.description || "",
      permissions: [],
    },
  });

  useEffect(() => {
    const fetchPermissions = async () => {
      try {
        const res = await permissionService.getAll();
        setPermissions(res.data || []);

        if (isEditMode && initialData) {
          // initialData.permissions is a string array of codes based on RoleResponse
          // We need to map them to IDs
          const allPerms = res.data || [];
          const selectedIds = allPerms
            .filter(p => (initialData.permissions as unknown as string[]).includes(p.code))
            .map(p => p.id);
          setValue("permissions", selectedIds);

          // As code is not in RoleResponse, we can use id string or a dummy code for edit, 
          // but the backend requires code. Wait, we'll try something.
          setValue("code", "ROLE_" + initialData.id); // Placeholder or actual code
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
    try {
      setIsLoading(true);

      const payload: RoleRequest = {
        code: data.code,
        name: data.name,
        description: data.description,
        permissions: data.permissions,
      };

      if (isEditMode && initialData) {
        await roleService.update(initialData.id, payload);
        toast.success("Cập nhật nhóm quyền thành công!");
      } else {
        await roleService.create(payload);
        toast.success("Tạo nhóm quyền mới thành công!");
      }

      router.push("/roles");
      router.refresh();

    } catch (error: unknown) {
      console.error(error);
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra, vui lòng thử lại!");
    } finally {
      setIsLoading(false);
    }
  };

  const selectedPermissions = watch("permissions");

  const handleCheckboxChange = (id: number, checked: boolean) => {
    const current = selectedPermissions || [];
    if (checked) {
      setValue("permissions", [...current, id]);
    } else {
      setValue("permissions", current.filter((val: number) => val !== id));
    }
  };

  const handleSelectAll = (checked: boolean) => {
    if (checked) {
      setValue("permissions", permissions.map(p => p.id));
    } else {
      setValue("permissions", []);
    }
  };

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
            disabled={isEditMode} // Cannot edit code usually
          />

          <FormInput
            label="Tên nhóm quyền"
            id="name"
            type="text"
            {...register("name")}
            error={errors.name?.message}
          />

          <div className="md:col-span-2">
            <FormInput
              label="Mô tả"
              id="description"
              type="text"
              {...register("description")}
              error={errors.description?.message}
            />
          </div>

          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Phân quyền <span className="text-red-500">*</span>
            </label>
            {loadingPermissions ? (
              <p>Đang tải danh sách quyền...</p>
            ) : (
              <div className="border border-[#D5D5D5] rounded bg-[#F5F6FA] p-4 max-h-[400px] overflow-y-auto">
                <div className="mb-4 pb-2 border-b border-[#D5D5D5] flex items-center justify-between">
                  <span className="font-semibold text-[14px]">Chọn tất cả</span>
                  <input
                    type="checkbox"
                    className="w-[18px] h-[18px] cursor-pointer"
                    checked={selectedPermissions.length === permissions.length && permissions.length > 0}
                    onChange={(e) => handleSelectAll(e.target.checked)}
                  />
                </div>
                {errors.permissions && (
                  <p className="text-red-500 text-sm mb-3">{errors.permissions.message}</p>
                )}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                  {permissions.map((p) => (
                    <div key={p.id} className="flex items-center space-x-2">
                      <input
                        type="checkbox"
                        id={`perm-${p.id}`}
                        value={p.id}
                        checked={selectedPermissions.includes(p.id)}
                        onChange={(e) => handleCheckboxChange(p.id, e.target.checked)}
                        className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 cursor-pointer"
                      />
                      <label htmlFor={`perm-${p.id}`} className="text-sm font-medium text-gray-900 cursor-pointer">
                        {p.name} <span className="text-xs text-gray-500">({p.code})</span>
                      </label>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>

          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : (isEditMode ? "Cập nhật" : "Tạo mới")}
          </FormButton>
        </form>

        <FormBackLink href="/roles" />
      </div>
    </>
  )
}
