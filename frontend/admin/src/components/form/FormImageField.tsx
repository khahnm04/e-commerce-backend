/* eslint-disable @next/next/no-img-element */
"use client";
import React, { useState, useCallback, useEffect, useRef } from "react";
import { useFilePond } from "@/hooks/useFilePond";
import { FormImageUpload } from "@/components/form/FormImageUpload";
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

interface FormImageFieldProps {
  label: string;
  id: string;
  initialUrl?: string;
  isReadOnly?: boolean;
  isEditMode?: boolean;
  noImageText?: string;
  onFileSelect?: (file: File | null) => void;
}

/**
 * Reusable image upload field with preview.
 * Replaces ~35 lines of duplicated state + JSX in every Form component.
 */
export const FormImageField = ({
  label,
  id,
  initialUrl = "",
  isReadOnly = false,
  isEditMode = false,
  noImageText = "Không có ảnh",
  onFileSelect,
}: FormImageFieldProps) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialUrl);
  const blobUrlRef = useRef<string | null>(null);

  // Cleanup blob URL on unmount to prevent memory leak
  useEffect(() => {
    return () => {
      if (blobUrlRef.current) {
        URL.revokeObjectURL(blobUrlRef.current);
      }
    };
  }, []);

  const handleAddFile = useCallback(
    (file: File) => {
      // Revoke previous blob URL before creating a new one
      if (blobUrlRef.current) {
        URL.revokeObjectURL(blobUrlRef.current);
      }
      const newUrl = URL.createObjectURL(file);
      blobUrlRef.current = newUrl;
      setSelectedFile(file);
      setPreviewUrl(newUrl);
      onFileSelect?.(file);
    },
    [onFileSelect],
  );

  const handleRemoveFile = useCallback(() => {
    if (blobUrlRef.current) {
      URL.revokeObjectURL(blobUrlRef.current);
      blobUrlRef.current = null;
    }
    setSelectedFile(null);
    setPreviewUrl(isEditMode ? initialUrl : "");
    onFileSelect?.(null);
  }, [isEditMode, initialUrl, onFileSelect]);

  useFilePond({
    onAddFile: handleAddFile,
    onRemoveFile: handleRemoveFile,
  });

  if (isReadOnly) {
    return (
      <div className="md:col-span-2">
        <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">{label}</label>
        {initialUrl ? (
          <img
            src={initialUrl}
            alt={label}
            className="w-[150px] h-auto object-cover rounded border"
          />
        ) : (
          <p className="text-gray-400 text-sm italic">{noImageText}</p>
        )}
      </div>
    );
  }

  return (
    <div className="md:col-span-2 flex items-center gap-[50px]">
      <FormImageUpload label={label} id={id} name={id} />
      {previewUrl && (
        <div
          className="flex flex-col items-start gap-[11px]
            font-[600] text-[14px] text-[#606060] h-full"
        >
          <span>{selectedFile ? "Ảnh mới:" : "Ảnh hiện tại:"}</span>
          <img
            src={previewUrl}
            alt={`${label} preview`}
            className="w-[150px] h-[150px] object-cover rounded-[8px]
              border border-[#D5D5D5]"
          />
        </div>
      )}
    </div>
  );
};
