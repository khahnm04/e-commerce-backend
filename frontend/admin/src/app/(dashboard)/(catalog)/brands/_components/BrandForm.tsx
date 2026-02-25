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
  CreateBrandSchema,
  UpdateBrandSchema,
  CreateBrandValues,
  UpdateBrandValues,
} from "@/app/(dashboard)/(catalog)/brands/brand.schema";
import { brandService } from "@/services/catalog/brand.service";
import { BrandResponse } from "@/types/catalog/brand.types";
import { uploadToCloudinary } from "@/services/upload.service";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

interface BrandFormProps {
  initialData?: BrandResponse;
  isReadOnly?: boolean;
}

export default function BrandForm({ initialData, isReadOnly = false }: BrandFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialData?.logoUrl || "");
  const isEditMode = !!initialData;

  const handleAddFile = useCallback((file: File) => {
    setSelectedFile(file);
    setPreviewUrl(URL.createObjectURL(file));
  }, []);

  const handleRemoveFile = useCallback(() => {
    setSelectedFile(null);
    setPreviewUrl(isEditMode ? (initialData?.logoUrl || "") : "");
  }, [isEditMode, initialData?.logoUrl]);

  useFilePond({
    onAddFile: handleAddFile,
    onRemoveFile: handleRemoveFile,
  });

  const schema = isEditMode ? UpdateBrandSchema : CreateBrandSchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateBrandValues | UpdateBrandValues>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: initialData?.name || "",
      slug: initialData?.slug || "",
      description: initialData?.description || "",
      logoUrl: initialData?.logoUrl || "",
      country: initialData?.country || "",
      websiteUrl: initialData?.websiteUrl || "",
      metaTitle: initialData?.metaTitle || "",
      metaDescription: initialData?.metaDescription || "",
      metaKeywords: initialData?.metaKeywords || "",
      status: initialData?.status || "ACTIVE",
    },
  });

  const onSubmit = async (data: CreateBrandValues | UpdateBrandValues) => {
    try {
      setIsLoading(true);

      // Upload logo to Cloudinary if a new file is selected
      let logoUrl = initialData?.logoUrl || "";
      if (selectedFile) {
        logoUrl = await uploadToCloudinary(selectedFile);
      }

      const payload = {
        name: data.name,
        slug: data.slug || data.name.toLowerCase().replace(/\s+/g, "-"),
        description: data.description,
        logoUrl: logoUrl || data.logoUrl,
        country: data.country,
        websiteUrl: data.websiteUrl,
        metaTitle: data.metaTitle,
        metaDescription: data.metaDescription,
        metaKeywords: data.metaKeywords,
        status: data.status,
      };

      if (isEditMode) {
        await brandService.update(initialData.id, payload);
        toast.success("Cập nhật nhãn hàng thành công!");
      } else {
        await brandService.create(payload);
        toast.success("Tạo nhãn hàng mới thành công!");
      }

      router.push("/brands");
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
        <FormInput
          label="Tên nhãn hàng"
          id="name"
          {...register("name")}
          error={errors.name?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Slug"
          id="slug"
          placeholder="Tự động tạo nếu để trống"
          {...register("slug")}
          error={errors.slug?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Quốc gia"
          id="country"
          {...register("country")}
          error={errors.country?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Website"
          id="websiteUrl"
          type="url"
          placeholder="https://..."
          {...register("websiteUrl")}
          error={errors.websiteUrl?.message}
          disabled={isReadOnly}
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

        <FormInput
          label="Mô tả"
          id="description"
          {...register("description")}
          error={errors.description?.message}
          disabled={isReadOnly}
        />

        {/* Logo upload */}
        {isReadOnly ? (
          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Logo
            </label>
            {initialData?.logoUrl ? (
              <img
                src={initialData.logoUrl}
                alt="Logo"
                className="w-[150px] h-auto object-cover rounded border"
              />
            ) : (
              <p className="text-gray-400 text-sm italic">Không có logo</p>
            )}
          </div>
        ) : (
          <div className="md:col-span-2 flex items-center gap-[50px]">
            <FormImageUpload
              label="Logo"
              id="logo"
              name="logo"
            />
            {previewUrl && (
              <div className="flex flex-col items-start gap-[11px] font-[600] text-[14px] text-[#606060] h-full">
                <span>{selectedFile ? "Ảnh mới:" : "Ảnh hiện tại:"}</span>
                <img
                  src={previewUrl}
                  alt="Logo preview"
                  className="w-[150px] h-[150px] object-cover rounded-[8px] border border-[#D5D5D5]"
                />
              </div>
            )}
          </div>
        )}

        {/* SEO fields */}
        <FormInput
          label="Meta Title"
          id="metaTitle"
          {...register("metaTitle")}
          error={errors.metaTitle?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Meta Description"
          id="metaDescription"
          {...register("metaDescription")}
          error={errors.metaDescription?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Meta Keywords"
          id="metaKeywords"
          {...register("metaKeywords")}
          error={errors.metaKeywords?.message}
          disabled={isReadOnly}
        />

        {/* Submit button */}
        {!isReadOnly && (
          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={isReadOnly ? "/brands/trash" : "/brands"} />
    </div>
  );
}
