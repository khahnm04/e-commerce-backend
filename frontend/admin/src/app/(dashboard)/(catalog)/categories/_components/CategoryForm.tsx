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
  CreateCategorySchema,
  UpdateCategorySchema,
  CreateCategoryValues,
  UpdateCategoryValues,
} from "@/app/(dashboard)/(catalog)/categories/category.schema";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import { uploadToCloudinary } from "@/services/upload.service";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

interface CategoryFormProps {
  initialData?: CategoryResponse;
  isReadOnly?: boolean;
}

export default function CategoryForm({ initialData, isReadOnly = false }: CategoryFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialData?.iconUrl || "");
  const [parentOptions, setParentOptions] = useState<{ id: string | number; name: string }[]>([
    { id: "", name: "Không có (Danh mục gốc)" },
  ]);
  const isEditMode = !!initialData;

  // Fetch parent category options
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await categoryService.getAll({ size: 99 });
        const categories = (res.data ?? [])
          .filter(c => !initialData || c.id !== initialData.id) // Exclude self
          .map(c => ({ id: String(c.id), name: c.name }));
        setParentOptions([{ id: "", name: "Không có (Danh mục gốc)" }, ...categories]);
      } catch {
        // ignore
      }
    };
    fetchCategories();
  }, [initialData]);

  const handleAddFile = useCallback((file: File) => {
    setSelectedFile(file);
    setPreviewUrl(URL.createObjectURL(file));
  }, []);

  const handleRemoveFile = useCallback(() => {
    setSelectedFile(null);
    setPreviewUrl(isEditMode ? (initialData?.iconUrl || "") : "");
  }, [isEditMode, initialData?.iconUrl]);

  useFilePond({
    onAddFile: handleAddFile,
    onRemoveFile: handleRemoveFile,
  });

  const schema = isEditMode ? UpdateCategorySchema : CreateCategorySchema;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateCategoryValues | UpdateCategoryValues>({
    resolver: zodResolver(schema),
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

  const onSubmit = async (data: CreateCategoryValues | UpdateCategoryValues) => {
    try {
      setIsLoading(true);

      // Upload icon to Cloudinary if a new file is selected
      let iconUrl = initialData?.iconUrl || "";
      if (selectedFile) {
        iconUrl = await uploadToCloudinary(selectedFile);
      }

      const payload = {
        name: data.name!,
        slug: data.slug || data.name!.toLowerCase().replace(/\s+/g, "-"),
        description: data.description,
        iconUrl: iconUrl || data.iconUrl,
        bannerUrl: data.bannerUrl,
        position: data.position ? Number(data.position) : 0,
        metaTitle: data.metaTitle,
        metaDescription: data.metaDescription,
        metaKeywords: data.metaKeywords,
        status: data.status,
        parentId: data.parentId ? Number(data.parentId) : null,
      };

      if (isEditMode) {
        await categoryService.update(initialData.id, payload);
        toast.success("Cập nhật danh mục thành công!");
      } else {
        await categoryService.create(payload);
        toast.success("Tạo danh mục mới thành công!");
      }

      router.push("/categories");
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
          label="Tên danh mục"
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

        <Controller
          control={control}
          name="parentId"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="parentId"
              >
                Danh mục cha
              </label>
              <CustomSelect
                options={parentOptions}
                value={field.value ? String(field.value) : ""}
                onChange={(val) => field.onChange(val ? Number(val) : null)}
                placeholder="Chọn danh mục cha"
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
          label="Vị trí"
          id="position"
          type="number"
          {...register("position")}
          error={errors.position?.message}
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

        {/* Icon upload */}
        {isReadOnly ? (
          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Ảnh biểu tượng
            </label>
            {initialData?.iconUrl ? (
              <img
                src={initialData.iconUrl}
                alt="Icon"
                className="w-[150px] h-auto object-cover rounded border"
              />
            ) : (
              <p className="text-gray-400 text-sm italic">Không có ảnh</p>
            )}
          </div>
        ) : (
          <div className="md:col-span-2 flex items-center gap-[50px]">
            <FormImageUpload
              label="Ảnh biểu tượng"
              id="icon"
              name="icon"
            />
            {previewUrl && (
              <div className="flex flex-col items-start gap-[11px] font-[600] text-[14px] text-[#606060] h-full">
                <span>{selectedFile ? "Ảnh mới:" : "Ảnh hiện tại:"}</span>
                <img
                  src={previewUrl}
                  alt="Icon preview"
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

      <FormBackLink href={isReadOnly ? "/categories/trash" : "/categories"} />
    </div>
  );
}
