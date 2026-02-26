"use client";
import React from "react";
import { Controller, Control } from "react-hook-form";
import { FormEditorWrapper } from "@/components/form/FormEditorWrapper";
import { useTinyMCE } from "@/hooks/useTinyMCE";

interface ProductDescriptionSectionProps {
  control: Control<any>;
  isReadOnly?: boolean;
  initialDescription?: string;
}

/**
 * Product description section with TinyMCE editor.
 * Displays read-only HTML content or an editable rich text editor.
 */
export const ProductDescriptionSection = ({
  control,
  isReadOnly = false,
  initialDescription = "",
}: ProductDescriptionSectionProps) => {
  const { TinyMCEEditorComponent } = useTinyMCE({
    initialValue: initialDescription,
    height: 350,
  });

  return (
    <div className="md:col-span-2">
      <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
        Mô tả chi tiết
      </label>
      {isReadOnly ? (
        <div
          className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] prose prose-sm max-w-none text-[14px] text-[var(--color-text)]"
          dangerouslySetInnerHTML={{ __html: initialDescription || '<p class="text-gray-400 italic">Chưa có mô tả</p>' }}
        />
      ) : (
        <Controller
          control={control}
          name="description"
          render={({ field }) => (
            <FormEditorWrapper label="">
              {React.cloneElement(TinyMCEEditorComponent as React.ReactElement<any>, {
                onEditorChange: (content: string) => {
                  field.onChange(content);
                },
              })}
            </FormEditorWrapper>
          )}
        />
      )}
    </div>
  );
};
