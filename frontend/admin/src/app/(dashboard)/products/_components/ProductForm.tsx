"use client"
import React from "react";
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
import { BRAND_OPTIONS, PRODUCT_STATUS_FORM_OPTIONS } from "@/constants/product.constants";
import { CreateProductSchema, CreateProductValues } from "@/lib/schemas/product.schema";

interface ProductFormProps {
  initialData?: any;
}

export default function ProductForm({ initialData }: ProductFormProps) {
  const { TinyMCEEditorComponent } = useTinyMCE();
  useFilePond();

  const isEditMode = !!initialData;

  const { register, control, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(CreateProductSchema),
    defaultValues: {
      name: initialData?.name || "",
      price: initialData?.price || 0,
      old_price: initialData?.old_price || 0,
      status: initialData?.status || "ACTIVE",
      brand_id: initialData?.brand_id || "",
      description: initialData?.description || "",
    },
  });

  const onSubmit = (data: CreateProductValues) => {
    console.log(isEditMode ? "Cập nhật sản phẩm:" : "Tạo sản phẩm:", data);
  };

  return (
    <>
      <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
        <form
          className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
          onSubmit={handleSubmit(onSubmit)}
        >
          <FormInput
            label="Tên sản phẩm"
            id="name"
            {...register("name")}
            error={errors.name?.message}
          />

          <Controller
            control={control}
            name="brand_id"
            render={({ field }) => (
              <div className="inner-group relative">
                <label className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]" htmlFor="brand_id">
                  Thương hiệu
                </label>
                <CustomSelect
                  options={BRAND_OPTIONS.map(opt => ({ id: opt.value, name: opt.label }))}
                  value={field.value ?? ""}
                  onChange={(val) => field.onChange(val)}
                  placeholder="Chọn thương hiệu"
                  classNameCustom="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] !block"
                />
              </div>
            )}
          />

          <FormInput
            label="Giá bán"
            id="price"
            type="number"
            {...register("price")}
            error={errors.price?.message}
          />

          <FormInput
            label="Giá cũ"
            id="old_price"
            type="number"
            {...register("old_price")}
            error={errors.old_price?.message}
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
                  options={PRODUCT_STATUS_FORM_OPTIONS.map(opt => ({ id: opt.value, name: opt.label }))}
                  value={field.value ?? ""}
                  onChange={(val) => field.onChange(val)}
                  placeholder="Chọn trạng thái"
                  classNameCustom="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] !block"
                />
              </div>
            )}
          />

          <FormImageUpload
            label="Ảnh Thumbnail"
            id="thumbnail"
            name="thumbnail"
          />

          {isEditMode && initialData?.thumbnail && (
            <div className="col-span-1 md:col-span-2 -mt-5 text-sm text-gray-500">
              Ảnh hiện tại:
              <a
                href={initialData.thumbnail}
                target="_blank"
                className="text-blue-500 underline text-xs"
              >
                Xem
              </a>
            </div>
          )}

          <FormEditorWrapper label="Mô tả sản phẩm">
            {TinyMCEEditorComponent}
          </FormEditorWrapper>

          <FormButton type="submit">
            {isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        </form>

        <FormBackLink href="/products" />
      </div>
    </>
  );
}
