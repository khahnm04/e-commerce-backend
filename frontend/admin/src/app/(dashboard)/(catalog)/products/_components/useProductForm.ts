import { useState, useEffect, useCallback } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useFormSubmit } from "@/hooks/useFormSubmit";
import {
  CreateProductSchema,
  UpdateProductSchema,
  CreateProductValues,
} from "@/app/(dashboard)/(catalog)/products/product.schema";
import { productService } from "@/services/catalog/product.service";
import { brandService } from "@/services/catalog/brand.service";
import { categoryService } from "@/services/catalog/category.service";
import { attributeService } from "@/services/catalog/attribute.service";
import { ProductResponse } from "@/types/catalog/product.types";
import { BrandResponse } from "@/types/catalog/brand.types";
import { CategoryResponse } from "@/types/catalog/category.types";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { ProductStatus } from "@/constants/enums/catalog.enums";
import { generateSlug } from "@/lib/format-utils";

export interface AttributeRow {
  id?: number;
  attributeId: number;
  value: string;
}

export interface ImageRow {
  id?: number;
  imageUrl: string;
  displayOrder: number;
  file?: File;
  previewUrl?: string;
  isUploading?: boolean;
}

export const useProductForm = (initialData?: ProductResponse, isReadOnly: boolean = false) => {
  const { isLoading, submitHandler } = useFormSubmit({ redirectTo: "/products" });

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [allBrands, setAllBrands] = useState<BrandResponse[]>([]);
  const [allCategories, setAllCategories] = useState<CategoryResponse[]>([]);
  const [allAttributes, setAllAttributes] = useState<AttributeResponse[]>([]);

  const [attributeRows, setAttributeRows] = useState<AttributeRow[]>(
    initialData?.attributes?.map((a) => ({
      id: a.id,
      attributeId: a.attributeId,
      value: a.value,
    })) || [],
  );

  const [imageRows, setImageRows] = useState<ImageRow[]>(
    initialData?.images?.map((img) => ({
      id: img.id,
      imageUrl: img.imageUrl,
      displayOrder: img.displayOrder,
      previewUrl: img.imageUrl,
    })) || [],
  );

  const isEditMode = !!initialData;

  useEffect(() => {
    if (isReadOnly) return;
    const fetchOptions = async () => {
      const [brandsRes, categoriesRes, attributesRes] = await Promise.allSettled([
        brandService.getAll({ size: 99 }),
        categoryService.getAll({ size: 99 }),
        attributeService.getAll({ size: 99 }),
      ]);
      if (brandsRes.status === "fulfilled") setAllBrands(brandsRes.value.data ?? []);
      if (categoriesRes.status === "fulfilled") setAllCategories(categoriesRes.value.data ?? []);
      if (attributesRes.status === "fulfilled") setAllAttributes(attributesRes.value.data ?? []);
    };
    fetchOptions();
  }, [isReadOnly]);

  const handleImageFileSelect = useCallback((file: File | null) => {
    setSelectedFile(file);
  }, []);

  const schema = isEditMode ? UpdateProductSchema : CreateProductSchema;

  const getDefaultCategoryIds = (): number[] => {
    if (!initialData?.categories) return [];
    return initialData.categories.map((c) => c.categoryId);
  };

  const formMethods = useForm<CreateProductValues>({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    resolver: zodResolver(schema as any),
    defaultValues: {
      name: initialData?.name || "",
      slug: initialData?.slug || "",
      productCode: initialData?.productCode || "",
      brandId: initialData?.brandId || 0,
      hasSerialNumber: initialData?.hasSerialNumber || false,
      warrantyMonths: initialData?.warrantyMonths || 0,
      price: initialData?.price || 0,
      oldPrice: initialData?.oldPrice || 0,
      thumbnail: initialData?.thumbnail || "",
      description: initialData?.description || "",
      shortDescription: initialData?.shortDescription || "",
      metaTitle: initialData?.metaTitle || "",
      metaDescription: initialData?.metaDescription || "",
      status: initialData?.status || "ACTIVE",
      categoryIds: [],
    },
  });

  const { setValue } = formMethods;

  useEffect(() => {
    if (allCategories.length > 0 && initialData?.categories) {
      const categoryIds = getDefaultCategoryIds();
      setValue("categoryIds", categoryIds);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allCategories]);

  const onSubmit = async (data: CreateProductValues) => {
    await submitHandler(
      async () => {
        let thumbnailUrl = initialData?.thumbnail || "";
        if (selectedFile) {
          thumbnailUrl = await uploadToCloudinary(selectedFile);
        }

        const uploadPromises = imageRows.map(async (img) => {
          if (img.file) {
            const url = await uploadToCloudinary(img.file);
            return { id: img.id, imageUrl: url, displayOrder: img.displayOrder };
          }
          return { id: img.id, imageUrl: img.imageUrl, displayOrder: img.displayOrder };
        });
        const uploadedImages = await Promise.all(uploadPromises);

        const payload = {
          name: data.name!,
          slug: data.slug || generateSlug(data.name!),
          productCode: data.productCode || undefined,
          brandId: Number(data.brandId),
          hasSerialNumber: data.hasSerialNumber,
          warrantyMonths: data.warrantyMonths ? Number(data.warrantyMonths) : 0,
          price: Number(data.price),
          oldPrice: data.oldPrice ? Number(data.oldPrice) : 0,
          thumbnail: thumbnailUrl || data.thumbnail || "",
          description: data.description,
          shortDescription: data.shortDescription,
          metaTitle: data.metaTitle,
          metaDescription: data.metaDescription,
          status: data.status as ProductStatus,
          categoryIds: data.categoryIds || [],
          attributes: attributeRows
            .filter((a) => a.attributeId && a.value)
            .map((a) => ({
              ...(a.id ? { id: a.id } : {}),
              attributeId: Number(a.attributeId),
              value: a.value,
            })),
          images: uploadedImages,
        };

        if (isEditMode) {
          await productService.update(initialData.id, payload);
        } else {
          await productService.create(payload);
        }
      },
      isEditMode ? "Cập nhật sản phẩm thành công!" : "Tạo sản phẩm mới thành công!",
    );
  };

  return {
    formMethods,
    onSubmit,
    isLoading,
    isEditMode,
    allBrands,
    allCategories,
    allAttributes,
    attributeRows,
    setAttributeRows,
    imageRows,
    setImageRows,
    handleImageFileSelect,
  };
};
