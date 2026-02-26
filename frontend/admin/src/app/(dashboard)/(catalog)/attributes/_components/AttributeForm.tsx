"use client";
import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateAttributeSchema,
  CreateAttributeValues,
} from "@/app/(dashboard)/(catalog)/attributes/attribute.schema";
import { attributeService } from "@/services/catalog/attribute.service";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import { useFormSubmit } from "@/hooks/useFormSubmit";

interface AttributeFormProps {
  initialData?: AttributeResponse;
}

export default function AttributeForm({ initialData }: AttributeFormProps) {
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/attributes" });
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
    await submitHandler(
      async () => {
        if (isEditMode) {
          await attributeService.update(initialData.id, {
            name: data.name,
            code: data.code,
            description: data.description,
          });
        } else {
          await attributeService.create({
            name: data.name,
            code: data.code,
            description: data.description,
          });
        }
      },
      isEditMode ? "Cập nhật thuộc tính thành công!" : "Tạo thuộc tính mới thành công!",
    );
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

        <FormButton
          type="submit"
          disabled={isLoading}
        >
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/attributes" />
    </div>
  );
}
