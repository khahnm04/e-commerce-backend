"use client";
import React, { useState, useCallback } from "react";
import { usePathname } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import {
  CreateBrandSchema,
  UpdateBrandSchema,
  CreateBrandValues,
} from "@/app/(dashboard)/(catalog)/brands/brand.schema";
import { brandService } from "@/services/catalog/brand.service";
import { BrandResponse } from "@/types/catalog/brand.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { BRAND_STATUS_OPTIONS } from "@/constants/catalog/brand.constants";
import { BrandStatus } from "@/constants/enums/catalog.enums";
import { getFormOptions } from "@/constants/common.constants";
import { generateSlug } from "@/lib/format-utils";
import { useFormSubmit } from "@/hooks/useFormSubmit";

const STATUS_OPTIONS = getFormOptions(BRAND_STATUS_OPTIONS);

interface BrandFormProps {
  initialData?: BrandResponse;
  isReadOnly?: boolean;
}

export default function BrandForm({ initialData, isReadOnly = false }: BrandFormProps) {
  const pathname = usePathname();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const isEditMode = !!initialData;
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/brands" });

  const handleFileSelect = useCallback((file: File | null) => {
    setSelectedFile(file);
  }, []);

  const schema = isEditMode ? UpdateBrandSchema : CreateBrandSchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateBrandValues>({
    resolver: zodResolver(schema as any),
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

  const onSubmit = async (data: CreateBrandValues) => {
    await submitHandler(
      async () => {
        let logoUrl = initialData?.logoUrl || "";
        if (selectedFile) {
          logoUrl = await uploadToCloudinary(selectedFile);
        }

        const payload = {
          name: data.name,
          slug: data.slug || generateSlug(data.name),
          description: data.description,
          logoUrl: logoUrl || data.logoUrl,
          country: data.country,
          websiteUrl: data.websiteUrl,
          metaTitle: data.metaTitle,
          metaDescription: data.metaDescription,
          metaKeywords: data.metaKeywords,
          status: data.status as BrandStatus,
        };

        if (isEditMode) {
          await brandService.update(initialData.id, payload);
        } else {
          await brandService.create(payload);
        }
      },
      isEditMode ? "Cập nhật nhãn hàng thành công!" : "Tạo nhãn hàng mới thành công!",
    );
  };

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={(e) => handleSubmit(onSubmit as any)(e).catch(() => { })}
      >
        <FormInput
          label="Tên nhãn hàng"
          id="name"
          {...register("name")}
          error={errors.name?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Đường dẫn tĩnh (Slug)"
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

        <FormControlledSelect
          control={control}
          name="status"
          label="Trạng thái"
          options={STATUS_OPTIONS}
          placeholder="Chọn trạng thái"
          disabled={isReadOnly}
        />

        <FormInput
          label="Mô tả"
          id="description"
          {...register("description")}
          error={errors.description?.message}
          disabled={isReadOnly}
        />

        {/* SEO fields */}
        <FormInput
          label="Tiêu đề SEO (Meta Title)"
          id="metaTitle"
          {...register("metaTitle")}
          error={errors.metaTitle?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Mô tả SEO (Meta Description)"
          id="metaDescription"
          {...register("metaDescription")}
          error={errors.metaDescription?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Từ khóa SEO (Meta Keywords)"
          id="metaKeywords"
          {...register("metaKeywords")}
          error={errors.metaKeywords?.message}
          disabled={isReadOnly}
        />

        {/* Logo upload */}
        <FormImageField
          label="Logo"
          id="logo"
          initialUrl={initialData?.logoUrl}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có logo"
          onFileSelect={handleFileSelect}
        />

        {/* Submit button */}
        {!isReadOnly && (
          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={pathname.includes("/trash") ? "/brands/trash" : "/brands"} />
    </div>
  );
}
