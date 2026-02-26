"use client";
import React, { useState, useEffect, useCallback } from "react";
import { useRouter, usePathname } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import { RoleSelectionField } from "@/components/form/RoleSelectionField";
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
import { getFormOptions, GENDER_FORM_OPTIONS } from "@/constants/common.constants";
import { EMPLOYEE_STATUS_OPTIONS } from "@/constants/user/employee.constants";
import { Gender, UserStatus } from "@/constants/enums";

const STATUS_OPTIONS = getFormOptions(EMPLOYEE_STATUS_OPTIONS);

interface EmployeeFormProps {
  initialData?: EmployeeResponse;
  isReadOnly?: boolean;
}

export default function EmployeeForm({ initialData, isReadOnly = false }: EmployeeFormProps) {
  const router = useRouter();
  const pathname = usePathname();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
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

  const handleFileSelect = useCallback((file: File | null) => {
    setSelectedFile(file);
  }, []);

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
  } = useForm<CreateEmployeeValues>({
    resolver: zodResolver(schema as any),
    shouldUnregister: true, // Auto unregister hidden fields
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
      password: "",
      confirmPassword: "",
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

  const onSubmit = async (data: CreateEmployeeValues) => {
    try {
      setIsLoading(true);

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
          gender: data.gender as Gender,
          status: data.status as UserStatus,
          roles: data.roles || [],
        });
        toast.success("Cập nhật nhân viên thành công!");
      } else {
        await employeeService.create({
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          password: data.password,
          confirmPassword: data.confirmPassword,
          code: data.code,
          branchId: data.branchId,
          jobTitle: data.jobTitle,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: avatarUrl,
          gender: data.gender as Gender,
          status: data.status as UserStatus,
          roles: data.roles || [],
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
        onSubmit={(e) => handleSubmit(onSubmit as any)(e).catch(() => { })}
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
        <FormControlledSelect
          control={control}
          name="gender"
          label="Giới tính"
          options={GENDER_FORM_OPTIONS}
          placeholder="Chọn giới tính"
          disabled={isReadOnly}
        />

        <FormControlledSelect
          control={control}
          name="status"
          label="Trạng thái"
          options={STATUS_OPTIONS}
          placeholder="Chọn trạng thái"
          disabled={isReadOnly}
        />

        {/* Row 5: Role selection - full width */}
        <RoleSelectionField
          control={control}
          name="roles"
          allRoles={allRoles}
          isReadOnly={isReadOnly}
          initialRoleNames={initialData?.roles}
          errors={errors}
        />

        {/* Row 6: Avatar upload - full width */}
        <FormImageField
          label="Ảnh đại diện"
          id="avatar"
          initialUrl={initialData?.avatarUrl}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có ảnh đại diện"
          onFileSelect={handleFileSelect}
        />

        {/* Row 7 (create only): Password + Confirm Password - LAST ROW */}
        {!isEditMode && !isReadOnly && (
          <>
            <FormInput
              label="Mật khẩu"
              id="password"
              type="password"
              {...register("password")}
              error={errors.password?.message}
            />

            <FormInput
              label="Xác nhận mật khẩu"
              id="confirmPassword"
              type="password"
              {...register("confirmPassword")}
              error={errors.confirmPassword?.message}
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

      <FormBackLink href={pathname.includes("/trash") ? "/employees/trash" : "/employees"} />
    </div>
  );
}
