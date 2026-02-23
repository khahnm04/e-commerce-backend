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
import { CreateBrandSchema, CreateBrandValues } from "@/app/(dashboard)/(catalog)/brands/brand.schema";
import { brandService } from "@/services/brand.service";
import { BrandResponse } from "@/types/brand.types";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

interface BrandFormProps {
  initialData?: BrandResponse;
}

export default function BrandForm({ initialData }: BrandFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const isEditMode = !!initialData;

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateBrandValues>({
    resolver: zodResolver(CreateBrandSchema),
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
    try {
      setIsLoading(true);

      if (isEditMode) {
        await brandService.update(initialData.id, {
          name: data.name,
          slug: data.slug || data.name.toLowerCase().replace(/\s+/g, "-"),
          description: data.description,
          logoUrl: data.logoUrl,
          country: data.country,
          websiteUrl: data.websiteUrl,
          metaTitle: data.metaTitle,
          metaDescription: data.metaDescription,
          metaKeywords: data.metaKeywords,
          status: data.status,
        });
        toast.success("Cập nhật nhãn hàng thành công!");
      } else {
        await brandService.create({
          name: data.name,
          slug: data.slug || data.name.toLowerCase().replace(/\s+/g, "-"),
          description: data.description,
          logoUrl: data.logoUrl,
          country: data.country,
          websiteUrl: data.websiteUrl,
          metaTitle: data.metaTitle,
          metaDescription: data.metaDescription,
          metaKeywords: data.metaKeywords,
          status: data.status,
        });
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
        onSubmit={handleSubmit(onSubmit)}
      >
        <FormInput
          label="Tên nhãn hàng"
          id="name"
          {...register("name")}
          error={errors.name?.message}
        />

        <FormInput
          label="Slug"
          id="slug"
          placeholder="Tự động tạo nếu để trống"
          {...register("slug")}
          error={errors.slug?.message}
        />

        <FormInput
          label="Quốc gia"
          id="country"
          {...register("country")}
          error={errors.country?.message}
        />

        <FormInput
          label="Website"
          id="websiteUrl"
          type="url"
          placeholder="https://..."
          {...register("websiteUrl")}
          error={errors.websiteUrl?.message}
        />

        <FormInput
          label="Logo URL"
          id="logoUrl"
          placeholder="https://..."
          {...register("logoUrl")}
          error={errors.logoUrl?.message}
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
          label="Mô tả"
          id="description"
          {...register("description")}
          error={errors.description?.message}
        />

        <FormInput
          label="Meta Title"
          id="metaTitle"
          {...register("metaTitle")}
          error={errors.metaTitle?.message}
        />

        <FormInput
          label="Meta Description"
          id="metaDescription"
          {...register("metaDescription")}
          error={errors.metaDescription?.message}
        />

        <FormInput
          label="Meta Keywords"
          id="metaKeywords"
          {...register("metaKeywords")}
          error={errors.metaKeywords?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/brands" />
    </div>
  );
}
