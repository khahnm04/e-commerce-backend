/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
"use client";
import React, { useState, useCallback } from "react";
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
  CreateCustomerSchema,
  UpdateCustomerSchema,
  CreateCustomerValues,
  UpdateCustomerValues,
} from "@/app/(dashboard)/(user)/customers/customer.schema";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";

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
  isReadOnly?: boolean;
}

export default function CustomerForm({ initialData, isReadOnly = false }: CustomerFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialData?.avatarUrl || "");
  const isEditMode = !!initialData;

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

      // Upload avatar to Cloudinary if a new file is selected
      let avatarUrl = initialData?.avatarUrl || "";
      if (selectedFile) {
        avatarUrl = await uploadToCloudinary(selectedFile);
      }

      if (isEditMode) {
        await customerService.update(initialData.id, {
          fullName: data.fullName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          dateOfBirth: data.dateOfBirth,
          avatarUrl: avatarUrl,
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
          avatarUrl: avatarUrl,
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
        onSubmit={(e) => handleSubmit(onSubmit)(e).catch(() => { })}
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

        {/* Row 4: Avatar upload - full width */}
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

        {/* Row 5 (create only): Password + Confirm Password - LAST ROW */}
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

      <FormBackLink href={isReadOnly ? "/customers/trash" : "/customers"} />
    </div>
  );
}
