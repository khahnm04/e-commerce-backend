"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateVariantSchema,
  CreateVariantValues,
} from "@/app/(dashboard)/(catalog)/variants/variant.schema";
import { variantService } from "@/services/catalog/variant.service";
import { VariantResponse } from "@/types/catalog/variant.types";
import { useFormSubmit } from "@/hooks/useFormSubmit";

interface VariantFormProps {
  initialData?: VariantResponse;
}

export default function VariantForm({ initialData }: VariantFormProps) {
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/variants" });
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateVariantValues>({
    resolver: zodResolver(CreateVariantSchema),
    defaultValues: {
      name: initialData?.name || "",
      code: initialData?.code || "",
    },
  });

  const onSubmit = async (data: CreateVariantValues) => {
    await submitHandler(
      async () => {
        if (isEditMode) {
          await variantService.update(initialData.id, {
            name: data.name,
            code: data.code,
          });
        } else {
          await variantService.create({
            name: data.name,
            code: data.code,
          });
        }
      },
      isEditMode ? "Cập nhật biến thể thành công!" : "Tạo biến thể mới thành công!",
    );
  };

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={handleSubmit(onSubmit)}
      >
        <FormInput
          label="Tên biến thể"
          id="name"
          {...register("name")}
          error={errors.name?.message}
        />

        <FormInput
          label="Mã biến thể"
          id="code"
          {...register("code")}
          error={errors.code?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/variants" />
    </div>
  );
}
