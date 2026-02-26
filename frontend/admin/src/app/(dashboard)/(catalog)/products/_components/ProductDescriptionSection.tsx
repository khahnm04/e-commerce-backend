"use client";
import React from "react";
import { Controller, Control } from "react-hook-form";
import { FormEditorWrapper } from "@/components/form/FormEditorWrapper";
import { useTinyMCE } from "@/hooks/useTinyMCE";

import { CreateProductValues } from "../product.schema";

interface ProductDescriptionSectionProps {
  control: Control<CreateProductValues>;
  isReadOnly?: boolean;
  initialDescription?: string;
}

const ReadOnlyDescription = ({ html }: { html: string }) => (
  <div
    className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] prose prose-sm max-w-none text-[14px] text-[var(--color-text)]"
    dangerouslySetInnerHTML={{
      __html: html || '<p class="text-gray-400 italic">Chưa có mô tả</p>',
    }}
  />
);

const EditableDescription = ({
  control,
  initialDescription,
}: {
  control: Control<CreateProductValues>;
  initialDescription: string;
}) => {
  const { TinyMCEEditorComponent } = useTinyMCE({
    initialValue: initialDescription,
    height: 350,
  });

  return (
    <Controller
      control={control}
      name="description"
      render={({ field }) => (
        <FormEditorWrapper label="">
          {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
          {React.cloneElement(TinyMCEEditorComponent as React.ReactElement<any>, {
            onEditorChange: (content: string) => {
              field.onChange(content);
            },
          })}
        </FormEditorWrapper>
      )}
    />
  );
};

export const ProductDescriptionSection = ({
  control,
  isReadOnly = false,
  initialDescription = "",
}: ProductDescriptionSectionProps) => (
  <div className="md:col-span-2">
    <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
      Mô tả chi tiết
    </label>
    {isReadOnly ? (
      <ReadOnlyDescription html={initialDescription} />
    ) : (
      <EditableDescription
        control={control}
        initialDescription={initialDescription}
      />
    )}
  </div>
);
