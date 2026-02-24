"use client";
import React, { useState, useEffect, useCallback } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateVariantValueSchema,
  CreateVariantValueValues,
} from "@/app/(dashboard)/(catalog)/variant-values/variant-value.schema";
import { variantValueService } from "@/services/catalog/variantValue.service";
import { variantService } from "@/services/catalog/variant.service";
import { VariantValueResponse } from "@/types/catalog/variant-value.types";
import { VariantResponse } from "@/types/catalog/variant.types";

interface VariantValueFormProps {
  initialData?: VariantValueResponse;
}

export default function VariantValueForm({ initialData }: VariantValueFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [variants, setVariants] = useState<VariantResponse[]>([]);
  const isEditMode = !!initialData;

  const loadVariants = useCallback(async () => {
    try {
      const res = await variantService.getAll();
      setVariants(res.data ?? []);
    } catch {
      console.error("Không thể tải danh sách biến thể");
    }
  }, []);

  useEffect(() => {
    loadVariants();
  }, [loadVariants]);

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateVariantValueValues>({
    resolver: zodResolver(CreateVariantValueSchema),
    defaultValues: {
      variantId: initialData?.variantId || 0,
      value: initialData?.value || "",
      code: initialData?.code || "",
      displayOrder: initialData?.displayOrder || 0,
    },
  });

  const onSubmit = async (data: CreateVariantValueValues) => {
    try {
      setIsLoading(true);

      const payload = {
        variantId: Number(data.variantId),
        value: data.value,
        code: data.code,
        displayOrder: data.displayOrder,
      };

      if (isEditMode) {
        await variantValueService.update(initialData.id, payload);
        toast.success("Cập nhật giá trị biến thể thành công!");
      } else {
        await variantValueService.create(payload);
        toast.success("Tạo giá trị biến thể mới thành công!");
      }

      router.push("/variant-values");
      router.refresh();
    } catch (error: unknown) {
      console.error(error);
      toast.error(error instanceof Error ? error.message : "Có lỗi xảy ra, vui lòng thử lại!");
    } finally {
      setIsLoading(false);
    }
  };

  const variantOptions = variants.map((v) => ({
    id: String(v.id),
    name: v.name,
  }));

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={handleSubmit(onSubmit)}
      >
        <Controller
          control={control}
          name="variantId"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="variantId"
              >
                Biến thể
              </label>
              <CustomSelect
                options={variantOptions}
                value={String(field.value) ?? ""}
                onChange={(val) => field.onChange(Number(val))}
                placeholder="Chọn biến thể"
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                `}
              />
              {errors.variantId && (
                <span className="text-red-500 text-[13px] mt-[6px] block font-medium">
                  {errors.variantId.message}
                </span>
              )}
            </div>
          )}
        />

        <FormInput
          label="Giá trị"
          id="value"
          {...register("value")}
          error={errors.value?.message}
        />

        <FormInput
          label="Mã"
          id="code"
          placeholder="Tự động tạo nếu để trống"
          {...register("code")}
          error={errors.code?.message}
        />

        <FormInput
          label="Thứ tự hiển thị"
          id="displayOrder"
          type="number"
          {...register("displayOrder")}
          error={errors.displayOrder?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/variant-values" />
    </div>
  );
}
