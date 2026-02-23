/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateEmployeeSchema,
  UpdateEmployeeSchema,
  CreateEmployeeValues,
  UpdateEmployeeValues,
} from "@/app/(dashboard)/(user)/employees/employee.schema";
import { employeeService } from "@/services/employee.service";
import { EmployeeResponse } from "@/types/employee.types";

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
}

export default function EmployeeForm({ initialData }: EmployeeFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const isEditMode = !!initialData;

  const schema = isEditMode ? UpdateEmployeeSchema : CreateEmployeeSchema;

  const {
    register,
    control,
    handleSubmit,
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

  const onSubmit = async (data: CreateEmployeeValues | UpdateEmployeeValues) => {
    try {
      setIsLoading(true);

      if (isEditMode) {
        await employeeService.update(initialData.id, {
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          code: data.code,
          branchId: data.branchId,
          jobTitle: data.jobTitle,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: data.avatarUrl || "",
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
          avatarUrl: createData.avatarUrl || "",
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
        onSubmit={handleSubmit(onSubmit)}
      >
        <FormInput
          label="Họ tên"
          id="fullName"
          {...register("fullName")}
          error={errors.fullName?.message}
        />

        <FormInput
          label="Mã nhân viên"
          id="code"
          {...register("code")}
          error={errors.code?.message}
        />

        <FormInput
          label="Email"
          id="email"
          type="email"
          {...register("email")}
          error={errors.email?.message}
        />

        <FormInput
          label="Số điện thoại"
          id="phoneNumber"
          {...register("phoneNumber")}
          error={errors.phoneNumber?.message}
        />

        {!isEditMode && (
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

        <FormInput
          label="Chức vụ"
          id="jobTitle"
          {...register("jobTitle")}
          error={errors.jobTitle?.message}
        />

        <FormInput
          label="Ngày sinh"
          id="dateOfBirth"
          type="date"
          {...register("dateOfBirth")}
          error={errors.dateOfBirth?.message}
        />

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
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
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
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                `}
              />
            </div>
          )}
        />

        <FormInput
          label="Avatar URL"
          id="avatarUrl"
          placeholder="https://..."
          {...register("avatarUrl")}
          error={errors.avatarUrl?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/employees" />
    </div>
  );
}
