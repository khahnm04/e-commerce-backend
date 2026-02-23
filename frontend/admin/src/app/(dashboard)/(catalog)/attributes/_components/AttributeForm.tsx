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
  CreateAttributeSchema,
  CreateAttributeValues,
} from "@/app/(dashboard)/(catalog)/attributes/attribute.schema";
import { attributeService } from "@/services/attribute.service";
import { AttributeResponse } from "@/types/attribute.types";

interface AttributeFormProps {
  initialData?: AttributeResponse;
}

export default function AttributeForm({ initialData }: AttributeFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateAttributeValues>({
    resolver: zodResolver(CreateAttributeSchema),
    defaultValues: {
      name: initialData?.name || "",
      code: initialData?.code || "",
      description: initialData?.description || "",
    },
  });

  const onSubmit = async (data: CreateAttributeValues) => {
    try {
      setIsLoading(true);

      if (isEditMode) {
        await attributeService.update(initialData.id, {
          name: data.name,
          code: data.code,
          description: data.description,
        });
        toast.success("Cập nhật thuộc tính thành công!");
      } else {
        await attributeService.create({
          name: data.name,
          code: data.code,
          description: data.description,
        });
        toast.success("Tạo thuộc tính mới thành công!");
      }

      router.push("/attributes");
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
          label="Tên thuộc tính"
          id="name"
          {...register("name")}
          error={errors.name?.message}
        />

        <FormInput
          label="Mã thuộc tính"
          id="code"
          {...register("code")}
          error={errors.code?.message}
        />

        <FormInput
          label="Mô tả"
          id="description"
          {...register("description")}
          error={errors.description?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/attributes" />
    </div>
  );
}
