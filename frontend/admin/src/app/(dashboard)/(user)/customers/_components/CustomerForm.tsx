"use client";
import React, { useState, useCallback, useRef } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import {
  CreateCustomerSchema,
  UpdateCustomerSchema,
  CreateCustomerValues,
  UpdateCustomerValues,
} from "@/app/(dashboard)/(user)/customers/customer.schema";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { getFormOptions, GENDER_FORM_OPTIONS } from "@/constants/common.constants";
import { CUSTOMER_STATUS_OPTIONS } from "@/constants/user/customer.constants";
import { Gender, UserStatus } from "@/constants/enums";

const STATUS_OPTIONS = getFormOptions(CUSTOMER_STATUS_OPTIONS);

interface CustomerFormProps {
  initialData?: CustomerResponse;
  isReadOnly?: boolean;
}

export default function CustomerForm({ initialData, isReadOnly = false }: CustomerFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const selectedFileRef = useRef<File | null>(null);
  const isEditMode = !!initialData;

  const handleFileSelect = useCallback((file: File | null) => {
    selectedFileRef.current = file;
  }, []);

  const schema = isEditMode ? UpdateCustomerSchema : CreateCustomerSchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateCustomerValues>({
    resolver: zodResolver(schema as any),
    defaultValues: {
      fullName: initialData?.fullName || "",
      email: initialData?.email || "",
      phoneNumber: initialData?.phoneNumber || "",
      dateOfBirth: initialData?.dateOfBirth || "",
      avatarUrl: initialData?.avatarUrl || "",
      gender: initialData?.gender || "MALE",
      status: initialData?.status || "ACTIVE",
      password: "",
      confirmPassword: "",
    },
  });

  const onSubmit = async (data: CreateCustomerValues) => {
    try {
      setIsLoading(true);

      let avatarUrl = initialData?.avatarUrl || "";
      if (selectedFileRef.current) {
        avatarUrl = await uploadToCloudinary(selectedFileRef.current);
      }

      if (isEditMode) {
        await customerService.update(initialData.id, {
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: avatarUrl,
          gender: data.gender as Gender,
          status: data.status as UserStatus,
        });
        toast.success("Cập nhật khách hàng thành công!");
      } else {
        await customerService.create({
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          password: data.password,
          confirmPassword: data.confirmPassword,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: avatarUrl,
          gender: data.gender as Gender,
          status: data.status as UserStatus,
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
        onSubmit={(e) => handleSubmit(onSubmit as any)(e).catch(() => { })}
      >
        {/* Row 1: Họ tên + Email */}
        <FormInput
          label="Họ tên"
          id="fullName"
          {...register("fullName")}
          error={errors.fullName?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Email"
          id="email"
          type="email"
          {...register("email")}
          error={errors.email?.message}
          disabled={isReadOnly}
        />

        {/* Row 2: SĐT + Ngày sinh */}
        <FormInput
          label="Số điện thoại"
          id="phoneNumber"
          {...register("phoneNumber")}
          error={errors.phoneNumber?.message}
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

        {/* Row 3: Giới tính + Trạng thái */}
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

        {/* Row 4: Avatar upload - full width */}
        <FormImageField
          label="Ảnh đại diện"
          id="avatar"
          initialUrl={initialData?.avatarUrl}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có ảnh đại diện"
          onFileSelect={handleFileSelect}
        />

        {/* Row 5 (create only): Password + Confirm Password - LAST ROW */}
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

      <FormBackLink href={isReadOnly ? "/customers/trash" : "/customers"} />
    </div>
  );
}
