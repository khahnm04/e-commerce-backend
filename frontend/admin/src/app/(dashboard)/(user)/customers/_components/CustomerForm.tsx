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
  CreateCustomerSchema,
  UpdateCustomerSchema,
  CreateCustomerValues,
  UpdateCustomerValues,
} from "@/app/(dashboard)/(user)/customers/customer.schema";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";

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

interface CustomerFormProps {
  initialData?: CustomerResponse;
}

export default function CustomerForm({ initialData }: CustomerFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const isEditMode = !!initialData;

  const schema = isEditMode ? UpdateCustomerSchema : CreateCustomerSchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateCustomerValues | UpdateCustomerValues>({
    resolver: zodResolver(schema),
    defaultValues: {
      fullName: initialData?.fullName || "",
      email: initialData?.email || "",
      phoneNumber: initialData?.phoneNumber || "",
      dateOfBirth: initialData?.dateOfBirth || "",
      avatarUrl: initialData?.avatarUrl || "",
      gender: initialData?.gender || "MALE",
      status: initialData?.status || "ACTIVE",
    },
  });

  const onSubmit = async (data: CreateCustomerValues | UpdateCustomerValues) => {
    try {
      setIsLoading(true);

      if (isEditMode) {
        await customerService.update(initialData.id, {
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: data.avatarUrl,
          gender: data.gender,
          status: data.status,
        });
        toast.success("Cập nhật khách hàng thành công!");
      } else {
        const createData = data as CreateCustomerValues;
        await customerService.create({
          fullName: createData.fullName,
          email: createData.email,
          phoneNumber: createData.phoneNumber,
          password: createData.password,
          confirmPassword: createData.confirmPassword,
          dateOfBirth: createData.dateOfBirth,
          avatarUrl: createData.avatarUrl || "",
          gender: createData.gender,
          status: createData.status,
        });
        toast.success("Tạo khách hàng mới thành công!");
      }

      router.push("/customers");
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

      <FormBackLink href="/customers" />
    </div>
  );
}
