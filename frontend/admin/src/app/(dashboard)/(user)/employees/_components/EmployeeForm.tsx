/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
"use client";
import React, { useState, useCallback, useEffect } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";
import { useFilePond } from "@/hooks/useFilePond";
import { FormInput } from "@/components/form/FormInput";
import { FormImageUpload } from "@/components/form/FormImageUpload";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateEmployeeSchema,
  UpdateEmployeeSchema,
  CreateEmployeeValues,
  UpdateEmployeeValues,
} from "@/app/(dashboard)/(user)/employees/employee.schema";
import { employeeService } from "@/services/user/employee.service";
import { EmployeeResponse } from "@/types/user/employee.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "BANNED", name: "Bị cấm" },
  { id: "LOCKED", name: "Bị khóa" },
];

const GENDER_OPTIONS = [
  { id: "MALE", name: "Nam" },
  { id: "FEMALE", name: "Nữ" },
  { id: "OTHER", name: "Khác" },
];

interface EmployeeFormProps {
  initialData?: EmployeeResponse;
  isReadOnly?: boolean;
}

export default function EmployeeForm({ initialData, isReadOnly = false }: EmployeeFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialData?.avatarUrl || "");
  const [allRoles, setAllRoles] = useState<RoleResponse[]>([]);
  const isEditMode = !!initialData;

  // Fetch all roles
  useEffect(() => {
    const fetchRoles = async () => {
      try {
        const res = await roleService.getAll({ size: 100 });
        setAllRoles(res.data ?? []);
      } catch {
        // ignore
      }
    };
    fetchRoles();
  }, []);

  const handleAddFile = useCallback((file: File) => {
    setSelectedFile(file);
    setPreviewUrl(URL.createObjectURL(file));
  }, []);

  const handleRemoveFile = useCallback(() => {
    setSelectedFile(null);
    setPreviewUrl(isEditMode ? (initialData?.avatarUrl || "") : "");
  }, [isEditMode, initialData?.avatarUrl]);

  useFilePond({
    onAddFile: handleAddFile,
    onRemoveFile: handleRemoveFile,
  });

  const schema = isEditMode ? UpdateEmployeeSchema : CreateEmployeeSchema;

  // Compute default selected role IDs based on initialData.roles (names) matched to allRoles
  const getDefaultRoleIds = (): number[] => {
    if (!initialData?.roles || allRoles.length === 0) return [];
    return allRoles
      .filter(r => initialData.roles.includes(r.name))
      .map(r => r.id);
  };

  const {
    register,
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<CreateEmployeeValues | UpdateEmployeeValues>({
    resolver: zodResolver(schema),
    defaultValues: {
      fullName: initialData?.fullName || "",
      email: initialData?.email || "",
      phoneNumber: initialData?.phoneNumber || "",
      code: initialData?.code || "",
      branchId: 1,
      jobTitle: initialData?.jobTitle || "",
      dateOfBirth: initialData?.dateOfBirth || "",
      avatarUrl: initialData?.avatarUrl || "",
      gender: initialData?.gender || "MALE",
      status: initialData?.status || "ACTIVE",
      roles: [],
    },
  });

  // When allRoles are loaded, set the default role IDs for edit mode
  useEffect(() => {
    if (allRoles.length > 0 && initialData?.roles) {
      const roleIds = getDefaultRoleIds();
      setValue("roles", roleIds);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allRoles]);

  const onSubmit = async (data: CreateEmployeeValues | UpdateEmployeeValues) => {
    try {
      setIsLoading(true);

      // Upload avatar to Cloudinary if a new file is selected
      let avatarUrl = initialData?.avatarUrl || "";
      if (selectedFile) {
        avatarUrl = await uploadToCloudinary(selectedFile);
      }

      if (isEditMode) {
        await employeeService.update(initialData.id, {
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          code: data.code,
          branchId: data.branchId,
          jobTitle: data.jobTitle,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: avatarUrl,
          gender: data.gender,
          status: data.status,
          roles: data.roles || [],
        });
        toast.success("Cập nhật nhân viên thành công!");
      } else {
        const createData = data as CreateEmployeeValues;
        await employeeService.create({
          fullName: createData.fullName,
          email: createData.email,
          phoneNumber: createData.phoneNumber,
          password: createData.password,
          confirmPassword: createData.confirmPassword,
          code: createData.code,
          branchId: createData.branchId,
          jobTitle: createData.jobTitle,
          dateOfBirth: createData.dateOfBirth,
          avatarUrl: avatarUrl,
          gender: createData.gender,
          status: createData.status,
          roles: createData.roles || [],
        });
        toast.success("Tạo nhân viên mới thành công!");
      }

      router.push("/employees");
      router.refresh();
    } catch (error: unknown) {
      console.error(error);
      toast.error(error instanceof Error ? error.message : "Có lỗi xảy ra, vui lòng thử lại!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={(e) => handleSubmit(onSubmit)(e).catch(() => { })}
      >
        {/* Row 1: Họ tên + Mã NV */}
        <FormInput
          label="Họ tên"
          id="fullName"
          {...register("fullName")}
          error={errors.fullName?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Mã nhân viên"
          id="code"
          {...register("code")}
          error={errors.code?.message}
          disabled={isReadOnly}
        />

        {/* Row 2: Email + SĐT */}
        <FormInput
          label="Email"
          id="email"
          type="email"
          {...register("email")}
          error={errors.email?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Số điện thoại"
          id="phoneNumber"
          {...register("phoneNumber")}
          error={errors.phoneNumber?.message}
          disabled={isReadOnly}
        />

        {/* Row 3: Chức vụ + Ngày sinh */}
        <FormInput
          label="Chức vụ"
          id="jobTitle"
          {...register("jobTitle")}
          error={errors.jobTitle?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Ngày sinh"
          id="dateOfBirth"
          type="date"
          {...register("dateOfBirth")}
          error={errors.dateOfBirth?.message}
          disabled={isReadOnly}
        />

        {/* Row 4: Giới tính + Trạng thái */}
        <Controller
          control={control}
          name="gender"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="gender"
              >
                Giới tính
              </label>
              <CustomSelect
                options={GENDER_OPTIONS}
                value={field.value ?? ""}
                onChange={(val) => field.onChange(val)}
                placeholder="Chọn giới tính"
                disabled={isReadOnly}
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                  ${isReadOnly ? 'opacity-60 cursor-not-allowed' : ''}
                `}
              />
            </div>
          )}
        />

        <Controller
          control={control}
          name="status"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="status"
              >
                Trạng thái
              </label>
              <CustomSelect
                options={STATUS_OPTIONS}
                value={field.value ?? ""}
                onChange={(val) => field.onChange(val)}
                placeholder="Chọn trạng thái"
                disabled={isReadOnly}
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                  ${isReadOnly ? 'opacity-60 cursor-not-allowed' : ''}
                `}
              />
            </div>
          )}
        />

        {/* Row 5: Role selection - full width */}
        <Controller
          control={control}
          name="roles"
          render={({ field }) => (
            <div className="md:col-span-2">
              <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
                Nhóm quyền
              </label>
              {isReadOnly ? (
                <div className="flex flex-wrap gap-[8px]">
                  {initialData?.roles && initialData.roles.length > 0 ? (
                    initialData.roles.map((roleName) => (
                      <span
                        key={roleName}
                        className="bg-blue-50 text-blue-700 border border-blue-200 rounded-[6px] px-[12px] py-[6px] text-[13px] font-[600]"
                      >
                        {roleName}
                      </span>
                    ))
                  ) : (
                    <p className="text-gray-400 text-sm italic">Chưa gán quyền</p>
                  )}
                </div>
              ) : (
                <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] flex flex-wrap gap-[12px]">
                  {allRoles.length === 0 ? (
                    <p className="text-gray-400 text-sm italic">Đang tải danh sách quyền...</p>
                  ) : (
                    allRoles.map((role) => {
                      const isChecked = (field.value || []).includes(role.id);
                      return (
                        <label
                          key={role.id}
                          className={`
                            inline-flex items-center gap-[8px] cursor-pointer
                            border rounded-[6px] px-[14px] py-[8px]
                            text-[13px] font-[600] transition-all
                            ${isChecked
                              ? "bg-blue-50 border-blue-400 text-blue-700"
                              : "bg-white border-[#D5D5D5] text-[#606060] hover:border-blue-300 hover:bg-blue-50/50"
                            }
                          `}
                        >
                          <input
                            type="checkbox"
                            checked={isChecked}
                            className="w-[16px] h-[16px] accent-blue-600 cursor-pointer"
                            onChange={(e) => {
                              const current = field.value || [];
                              if (e.target.checked) {
                                field.onChange([...current, role.id]);
                              } else {
                                field.onChange(current.filter((id: number) => id !== role.id));
                              }
                            }}
                          />
                          {role.name}
                        </label>
                      );
                    })
                  )}
                </div>
              )}
              {(errors as any).roles?.message && (
                <p className="text-red-500 text-[13px] mt-[6px]">{(errors as any).roles.message}</p>
              )}
            </div>
          )}
        />

        {/* Row 6: Avatar upload - full width */}
        {isReadOnly ? (
          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Ảnh đại diện
            </label>
            {initialData?.avatarUrl ? (
              <img
                src={initialData.avatarUrl}
                alt="Avatar"
                className="w-[150px] h-auto object-cover rounded border"
              />
            ) : (
              <p className="text-gray-400 text-sm italic">Không có ảnh đại diện</p>
            )}
          </div>
        ) : (
          <div className="md:col-span-2 flex items-center gap-[50px]">
            <FormImageUpload
              label="Ảnh đại diện"
              id="avatar"
              name="avatar"
            />
            {/* Preview block next to upload */}
            {previewUrl && (
              <div className="flex flex-col items-start gap-[11px] font-[600] text-[14px] text-[#606060] h-full">
                <span>{selectedFile ? "Ảnh mới:" : "Ảnh hiện tại:"}</span>
                <img
                  src={previewUrl}
                  alt="Avatar preview"
                  className="w-[150px] h-[150px] object-cover rounded-[8px] border border-[#D5D5D5]"
                />
              </div>
            )}
          </div>
        )}

        {/* Row 7 (create only): Password + Confirm Password - LAST ROW */}
        {!isEditMode && !isReadOnly && (
          <>
            <FormInput
              label="Mật khẩu"
              id="password"
              type="password"
              {...register("password" as any)}
              error={(errors as any).password?.message}
            />

            <FormInput
              label="Xác nhận mật khẩu"
              id="confirmPassword"
              type="password"
              {...register("confirmPassword" as any)}
              error={(errors as any).confirmPassword?.message}
            />
          </>
        )}

        {/* Submit button */}
        {!isReadOnly && (
          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={isReadOnly ? "/employees/trash" : "/employees"} />
    </div>
  );
}
