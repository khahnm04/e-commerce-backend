/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
"use client"
import React from "react";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import "filepond/dist/filepond.min.css";
import 'filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css';
import { useFilePond } from "@/hooks/useFilePond";
import { useTinyMCE } from "@/hooks/useTinyMCE";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormImageUpload } from "@/components/form/FormImageUpload";
import { FormEditorWrapper } from "@/components/form/FormEditorWrapper";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { parentOptions, statusOptions } from "@/constants/category.constants";
import {
  CreateCategorySchema,
  CreateCategoryValues
} from "@/app/(dashboard)/(catalog)/categories/category.schema";
import { categoryService } from "@/services/category.service";
import { CategoryPayload } from "@/types/category.types";
import { CategoryStatus } from "@/constants/enums";

interface CategoryFormProps {
  initialData?: any;
}

export default function CategoryForm({ initialData }: CategoryFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [description, setDescription] = useState(initialData?.description || "");

  const { TinyMCEEditorComponent } = useTinyMCE();
  useFilePond();

  const isEditMode = !!initialData;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(CreateCategorySchema),
    defaultValues: {
      name: initialData?.name || "",
      position: initialData?.position || 0,
      slug: initialData?.slug || "",
      status: (isEditMode
        ? (statusOptions.find(opt => opt.label === initialData?.status)?.value || "active")
        : "active") as "active" | "inactive",
      parent: initialData?.parent || "",
    },
  });

  const onSubmit = async (data: CreateCategoryValues) => {
    try {
      setIsLoading(true);

      if (!isEditMode && !selectedFile) {
        toast.error("Vui lòng chọn ảnh đại diện!");
        setIsLoading(false);
        return;
      }

      const payload: CategoryPayload = {
        name: data.name,
        slug: data.slug ? data.slug : null,
        description: description,
        status: data.status ? (data.status as CategoryStatus) : CategoryStatus.ACTIVE,
        parentId: data.parent ? Number(data.parent) : null,
        position: data.position ? Number(data.position) : 0,
      };

      if (isEditMode) {
        if (!initialData?.id) {
          toast.error("Không tìm thấy ID!");
          return;
        }
        await categoryService.update(initialData.id, payload, selectedFile);
        toast.success("Cập nhật danh mục thành công!");
      } else {
        await categoryService.create(payload, selectedFile as File);
        toast.success("Tạo danh mục mới thành công!");
      }

      router.push("/categories");
      router.refresh();

    } catch (error: any) {
      console.error(error);
      toast.error(error?.message || "Có lỗi xảy ra, vui lòng thử lại!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
        <form
          id="category-form"
          className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
          onSubmit={handleSubmit(onSubmit)}
        >
          <FormInput
            label="Tên danh mục"
            id="name"
            type="text"
            {...register("name")}
            error={errors.name?.message}
          />

          <FormInput
            label="Mã định danh URL"
            id="slug"
            type="text"
            {...register("slug")}
            error={errors.slug?.message}
          />

          <Controller
            control={control}
            name="parent"
            render={({ field }) => (
              <div className="inner-group relative">
                <label className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]" htmlFor="parent">
                  Danh mục cha
                </label>
                <CustomSelect
                  options={parentOptions.map(opt => ({ id: opt.value, name: opt.label }))}
                  value={field.value ?? ""}
                  onChange={(val) => field.onChange(val)}
                  placeholder="Chọn danh mục"
                  classNameCustom="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] !block"
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
          />

          <Controller
            control={control}
            name="status"
            render={({ field }) => (
              <div className="inner-group relative">
                <label className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]" htmlFor="status">
                  Trạng thái
                </label>
                <CustomSelect
                  options={statusOptions.map(opt => ({ id: opt.value, name: opt.label }))}
                  value={field.value ?? ""}
                  onChange={(val) => field.onChange(val)}
                  placeholder="Chọn trạng thái"
                  classNameCustom="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] !block"
                />
              </div>
            )}
          />

          <div className="md:col-span-2 flex items-center gap-[50px]">
            <FormImageUpload
              label="Ảnh đại diện"
              id="avatar"
              name="avatar"
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                if (e.target.files && e.target.files[0]) {
                  setSelectedFile(e.target.files[0]);
                  console.log("Đã chọn file:", e.target.files[0].name);
                }
              }}
            />
            {isEditMode && initialData?.image && !selectedFile && (
              <div className="flex flex-col items-start gap-[11px] font-[600] text-[14px] text-[#606060] h-full">
                <span>Ảnh hiện tại:</span>
                <img
                  src={initialData.image}
                  alt="Current"
                  className="w-[150px] h-auto object-cover rounded border"
                />
              </div>
            )}
          </div>

          <FormEditorWrapper label="Mô tả">
            <div onBlur={(e: any) => setDescription(e.target.getContent())}>
              {TinyMCEEditorComponent}
            </div>
          </FormEditorWrapper>

          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : (isEditMode ? "Cập nhật" : "Tạo mới")}
          </FormButton>
        </form>

        <FormBackLink href="/category" />
      </div>
    </>
  )
}
