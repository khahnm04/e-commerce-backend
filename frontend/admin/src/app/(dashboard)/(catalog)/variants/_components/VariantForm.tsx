"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateVariantSchema,
  CreateVariantValues,
} from "@/app/(dashboard)/(catalog)/variants/variant.schema";
import { variantService } from "@/services/variant.service";
import { VariantResponse } from "@/types/variant.types";

interface VariantFormProps {
  initialData?: VariantResponse;
}

export default function VariantForm({ initialData }: VariantFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
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
    try {
      setIsLoading(true);

      if (isEditMode) {
        await variantService.update(initialData.id, {
          name: data.name,
          code: data.code,
        });
        toast.success("Cập nhật biến thể thành công!");
      } else {
        await variantService.create({
          name: data.name,
          code: data.code,
        });
        toast.success("Tạo biến thể mới thành công!");
      }

      router.push("/variants");
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
