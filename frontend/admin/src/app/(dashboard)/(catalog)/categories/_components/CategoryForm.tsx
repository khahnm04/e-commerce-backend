"use client";
import React, { useState, useEffect, useCallback } from "react";
import { usePathname } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import { FormReadOnlySelect } from "@/components/form/FormReadOnlySelect";
import {
  CreateCategorySchema,
  UpdateCategorySchema,
  CreateCategoryValues,
} from "@/app/(dashboard)/(catalog)/categories/category.schema";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import { CATEGORY_STATUS_OPTIONS } from "@/constants/catalog/category.constants";
import { CategoryStatus } from "@/constants/enums";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { getFormOptions } from "@/constants/common.constants";
import { generateSlug } from "@/lib/format-utils";
import { useFormSubmit } from "@/hooks/useFormSubmit";

const STATUS_OPTIONS = getFormOptions(CATEGORY_STATUS_OPTIONS);

interface CategoryFormProps {
  initialData?: CategoryResponse;
  isReadOnly?: boolean;
}

export default function CategoryForm({ initialData, isReadOnly = false }: CategoryFormProps) {
  const pathname = usePathname();
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/categories" });
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [parentOptions, setParentOptions] = useState<{ id: string | number; name: string }[]>([
    { id: "", name: "Không có (Danh mục gốc)" },
  ]);
  const isEditMode = !!initialData;

  // Fetch parent category options (skip in read-only mode)
  useEffect(() => {
    if (isReadOnly) return;
    const fetchCategories = async () => {
      try {
        const res = await categoryService.getAll({ size: 99 });
        const categories = (res.data ?? [])
          .filter((c) => !initialData || c.id !== initialData.id) // Exclude self
          .map((c) => ({ id: String(c.id), name: c.name }));
        setParentOptions([{ id: "", name: "Không có (Danh mục gốc)" }, ...categories]);
      } catch {
        // ignore
      }
    };
    fetchCategories();
  }, [initialData, isReadOnly]);

  const handleFileSelect = useCallback((file: File | null) => {
    setSelectedFile(file);
  }, []);

  const schema = isEditMode ? UpdateCategorySchema : CreateCategorySchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateCategoryValues>({
    resolver: zodResolver(schema as any),
    defaultValues: {
      name: initialData?.name || "",
      slug: initialData?.slug || "",
      description: initialData?.description || "",
      iconUrl: initialData?.iconUrl || "",
      bannerUrl: initialData?.bannerUrl || "",
      position: initialData?.position || 0,
      metaTitle: initialData?.metaTitle || "",
      metaDescription: initialData?.metaDescription || "",
      metaKeywords: initialData?.metaKeywords || "",
      status: initialData?.status || "ACTIVE",
      parentId: initialData?.parentId || null,
    },
  });

  const onSubmit = async (data: CreateCategoryValues) => {
    await submitHandler(
      async () => {
        let iconUrl = initialData?.iconUrl || "";
        if (selectedFile) {
          iconUrl = await uploadToCloudinary(selectedFile);
        }

        const payload = {
          name: data.name!,
          slug: data.slug || generateSlug(data.name!),
          description: data.description,
          iconUrl: iconUrl || data.iconUrl,
          bannerUrl: data.bannerUrl,
          position: data.position ? Number(data.position) : 0,
          metaTitle: data.metaTitle,
          metaDescription: data.metaDescription,
          metaKeywords: data.metaKeywords,
          status: data.status as CategoryStatus,
          parentId: data.parentId ? Number(data.parentId) : null,
        };

        if (isEditMode) {
          await categoryService.update(initialData.id, payload);
        } else {
          await categoryService.create(payload);
        }
      },
      isEditMode ? "Cập nhật danh mục thành công!" : "Tạo danh mục mới thành công!",
    );
  };

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={(e) => handleSubmit(onSubmit as any)(e).catch(() => { })}
      >
        <FormInput
          label="Tên danh mục"
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

        <FormReadOnlySelect
          control={control}
          name="parentId"
          label="Danh mục cha"
          options={parentOptions}
          placeholder="Chọn danh mục cha"
          readOnlyText={initialData?.parentName || "Không có (Danh mục gốc)"}
          isReadOnly={isReadOnly}
          emptyValue={null}
        />

        <FormInput
          label="Vị trí"
          id="position"
          type="number"
          {...register("position")}
          error={errors.position?.message}
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

        {/* Icon upload */}
        <FormImageField
          label="Ảnh biểu tượng"
          id="icon"
          initialUrl={initialData?.iconUrl}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có ảnh"
          onFileSelect={handleFileSelect}
        />

        {/* Submit button */}
        {!isReadOnly && (
          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={pathname.includes("/trash") ? "/categories/trash" : "/categories"} />
    </div>
  );
}
