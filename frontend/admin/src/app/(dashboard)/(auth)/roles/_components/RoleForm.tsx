"use client"
import React, { useEffect, useState, useMemo } from "react";
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
import { FaChevronDown } from "react-icons/fa6";

interface RoleFormProps {
  initialData?: RoleResponse;
  isReadOnly?: boolean;
}

export default function RoleForm({ initialData, isReadOnly = false }: RoleFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [permissions, setPermissions] = useState<PermissionResponse[]>([]);
  const [loadingPermissions, setLoadingPermissions] = useState(true);
  const [expandedModules, setExpandedModules] = useState<Set<string>>(new Set());
  const [expandedSubGroups, setExpandedSubGroups] = useState<Set<string>>(new Set());

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
          // Map permission codes from RoleResponse to IDs
          const selectedIds = allPerms
            .filter(p => (initialData.permissions as unknown as string[]).includes(p.code))
            .map(p => p.id);
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

  // Group permissions by module, then by sub-prefix
  const groupedPermissions = useMemo(() => {
    const groups: Record<string, Record<string, PermissionResponse[]>> = {};
    permissions.forEach(p => {
      const moduleName = p.module || "Khác";
      if (!groups[moduleName]) groups[moduleName] = {};

      const parts = p.code.split('_');
      const subName = parts.length > 1 ? parts.slice(0, parts.length - 1).join('_') : "Chung";

      if (!groups[moduleName][subName]) groups[moduleName][subName] = [];
      groups[moduleName][subName].push(p);
    });
    return groups;
  }, [permissions]);

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

  const toggleModule = (module: string) => {
    setExpandedModules(prev => {
      const next = new Set(prev);
      if (next.has(module)) {
        next.delete(module);
      } else {
        next.add(module);
      }
      return next;
    });
  };

  const toggleSubGroup = (subGroupKey: string) => {
    setExpandedSubGroups(prev => {
      const next = new Set(prev);
      if (next.has(subGroupKey)) {
        next.delete(subGroupKey);
      } else {
        next.add(subGroupKey);
      }
      return next;
    });
  };

  // Check if all permissions in a module are selected
  const isModuleAllSelected = (modulePerms: PermissionResponse[]) => {
    return modulePerms.every(p => selectedPermissions.includes(p.id));
  };

  // Check if some permissions in a module are selected
  const isModulePartialSelected = (modulePerms: PermissionResponse[]) => {
    const selected = modulePerms.filter(p => selectedPermissions.includes(p.id));
    return selected.length > 0 && selected.length < modulePerms.length;
  };

  // Toggle all permissions in a module
  const handleModuleSelectAll = (modulePerms: PermissionResponse[], checked: boolean) => {
    const current = selectedPermissions || [];
    const moduleIds = modulePerms.map(p => p.id);
    if (checked) {
      const newIds = [...new Set([...current, ...moduleIds])];
      setValue("permissions", newIds);
    } else {
      setValue("permissions", current.filter((id: number) => !moduleIds.includes(id)));
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
              Phân quyền <span className="text-red-500">*</span>
            </label>
            {loadingPermissions ? (
              <p>Đang tải danh sách quyền...</p>
            ) : (
              <div className="border border-[#D5D5D5] rounded-[10px] bg-[#F5F6FA] p-4 max-h-[500px] overflow-y-auto">
                {/* Select All */}
                <div className="mb-3 pb-3 border-b border-[#D5D5D5] flex items-center justify-between">
                  <span className="font-semibold text-[14px]">Chọn tất cả</span>
                  <input
                    type="checkbox"
                    className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
                    checked={selectedPermissions.length === permissions.length && permissions.length > 0}
                    onChange={(e) => handleSelectAll(e.target.checked)}
                    disabled={isReadOnly}
                  />
                </div>

                {errors.permissions && (
                  <p className="text-red-500 text-sm mb-3">{errors.permissions.message}</p>
                )}

                {/* Grouped by module */}
                <div className="space-y-4">
                  {Object.entries(groupedPermissions).map(([module, subGroups]) => {
                    const isExpanded = expandedModules.has(module);
                    const modulePerms = Object.values(subGroups).flat();
                    const allSelected = isModuleAllSelected(modulePerms);
                    const partialSelected = isModulePartialSelected(modulePerms);

                    return (
                      <div key={module} className="border border-[#E0E0E0] rounded-[8px] bg-white overflow-hidden">
                        {/* Module header */}
                        <div
                          className="flex items-center justify-between px-4 py-3 cursor-pointer hover:bg-gray-50 transition-colors"
                          onClick={() => toggleModule(module)}
                        >
                          <div className="flex items-center gap-3">
                            <FaChevronDown
                              className={`text-[12px] text-gray-500 transition-transform duration-200 ${isExpanded ? "" : "-rotate-90"}`}
                            />
                            <span className="font-[600] text-[14px] text-[var(--color-text)]">
                              {module}
                            </span>
                            <span className="text-[12px] text-gray-400">
                              ({modulePerms.filter(p => selectedPermissions.includes(p.id)).length}/{modulePerms.length})
                            </span>
                          </div>
                          <input
                            type="checkbox"
                            className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
                            checked={allSelected}
                            disabled={isReadOnly}
                            ref={el => {
                              if (el) el.indeterminate = partialSelected;
                            }}
                            onChange={(e) => {
                              e.stopPropagation();
                              handleModuleSelectAll(modulePerms, e.target.checked);
                            }}
                            onClick={(e) => e.stopPropagation()}
                          />
                        </div>

                        {/* Permissions list */}
                        {isExpanded && (
                          <div className="px-4 pb-4 pt-3 border-t border-[#F0F0F0] space-y-3 bg-[#F9FAFB]">
                            {Object.entries(subGroups).map(([subName, perms]) => {
                              const subGroupKey = `${module}-${subName}`;
                              const isSubExpanded = expandedSubGroups.has(subGroupKey);
                              const allSubSelected = isModuleAllSelected(perms);
                              const partialSubSelected = isModulePartialSelected(perms);

                              return (
                                <div key={subName} className="border border-[#E5E7EB] rounded-[6px] bg-white overflow-hidden shadow-sm">
                                  <div
                                    className="flex items-center justify-between px-3 py-2 border-b border-[#E5E7EB] bg-[#F3F4F6] cursor-pointer hover:bg-[#EAEBEE] transition-colors"
                                    onClick={() => toggleSubGroup(subGroupKey)}
                                  >
                                    <div className="flex items-center gap-2">
                                      <FaChevronDown
                                        className={`text-[10px] text-gray-500 transition-transform duration-200 ${isSubExpanded ? "" : "-rotate-90"}`}
                                      />
                                      <span className="font-semibold text-[13px] text-gray-700">{subName}</span>
                                      <span className="text-[11px] text-gray-400 font-normal">
                                        ({perms.filter(p => selectedPermissions.includes(p.id)).length}/{perms.length})
                                      </span>
                                    </div>
                                    <input
                                      type="checkbox"
                                      className="w-[16px] h-[16px] cursor-pointer accent-blue-600"
                                      checked={allSubSelected}
                                      disabled={isReadOnly}
                                      ref={el => {
                                        if (el) el.indeterminate = partialSubSelected;
                                      }}
                                      onChange={(e) => {
                                        e.stopPropagation();
                                        handleModuleSelectAll(perms, e.target.checked);
                                      }}
                                      onClick={(e) => e.stopPropagation()}
                                    />
                                  </div>

                                  {isSubExpanded && (
                                    <div className="p-3 grid grid-cols-1 md:grid-cols-2 gap-2">
                                      {perms.map((p) => (
                                        <div key={p.id} className="flex items-center space-x-2 py-1">
                                          <input
                                            type="checkbox"
                                            id={`perm-${p.id}`}
                                            value={p.id}
                                            checked={selectedPermissions.includes(p.id)}
                                            onChange={(e) => handleCheckboxChange(p.id, e.target.checked)}
                                            className="w-4 h-4 cursor-pointer accent-blue-600"
                                            disabled={isReadOnly}
                                          />
                                          <label htmlFor={`perm-${p.id}`} className="text-sm font-medium text-gray-700 cursor-pointer">
                                            {p.name}
                                          </label>
                                        </div>
                                      ))}
                                    </div>
                                  )}
                                </div>
                              );
                            })}
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </div>

          {!isReadOnly && (
            <FormButton type="submit" disabled={isLoading}>
              {isLoading ? "Đang xử lý..." : (isEditMode ? "Cập nhật" : "Tạo mới")}
            </FormButton>
          )}
        </form>

        <FormBackLink href={isReadOnly ? "/roles/trash" : "/roles"} />
      </div>
    </>
  )
}
