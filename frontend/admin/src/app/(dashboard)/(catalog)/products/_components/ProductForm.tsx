"use client";
import React, { useState, useEffect, useCallback, useRef } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import { FormEditorWrapper } from "@/components/form/FormEditorWrapper";
import { CustomSelect } from "@/components/common/CustomSelect";
import {
  CreateProductSchema,
  UpdateProductSchema,
  CreateProductValues,
  UpdateProductValues,
} from "@/app/(dashboard)/(catalog)/products/product.schema";
import { productService } from "@/services/catalog/product.service";
import { ProductResponse } from "@/types/catalog/product.types";
import { brandService } from "@/services/catalog/brand.service";
import { categoryService } from "@/services/catalog/category.service";
import { attributeService } from "@/services/catalog/attribute.service";
import { BrandResponse } from "@/types/catalog/brand.types";
import { CategoryResponse } from "@/types/catalog/category.types";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import { uploadToCloudinary } from "@/services/upload/upload.service";
import { getFormOptions } from "@/constants/common.constants";
import { PRODUCT_STATUS_OPTIONS } from "@/constants/catalog/product.constants";
import { ProductStatus } from "@/constants/enums/catalog.enums";
import { useTinyMCE } from "@/hooks/useTinyMCE";
import { ProductAttributesSection } from "./ProductAttributesSection";
import { ProductImagesSection } from "./ProductImagesSection";
import { ProductCategoriesSection } from "./ProductCategoriesSection";

const STATUS_OPTIONS = getFormOptions(PRODUCT_STATUS_OPTIONS);

interface AttributeRow {
  id?: number;
  attributeId: number;
  value: string;
}

interface ImageRow {
  id?: number;
  imageUrl: string;
  displayOrder: number;
  file?: File;
  previewUrl?: string;
  isUploading?: boolean;
}

interface ProductFormProps {
  initialData?: ProductResponse;
  isReadOnly?: boolean;
}

export default function ProductForm({ initialData, isReadOnly = false }: ProductFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const selectedFileRef = useRef<File | null>(null);
  const [allBrands, setAllBrands] = useState<BrandResponse[]>([]);
  const [allCategories, setAllCategories] = useState<CategoryResponse[]>([]);
  const [allAttributes, setAllAttributes] = useState<AttributeResponse[]>([]);
  const [attributeRows, setAttributeRows] = useState<AttributeRow[]>(
    initialData?.attributes?.map(a => ({ id: a.id, attributeId: a.attributeId, value: a.value })) || []
  );
  const [imageRows, setImageRows] = useState<ImageRow[]>(
    initialData?.images?.map(img => ({ id: img.id, imageUrl: img.imageUrl, displayOrder: img.displayOrder, previewUrl: img.imageUrl })) || []
  );
  const isEditMode = !!initialData;

  // Fetch all options in a single effect (skip in read-only mode)
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

  // TinyMCE editor for description
  const { TinyMCEEditorComponent } = useTinyMCE({
    initialValue: initialData?.description || "",
    height: 350,
  });

  const handleImageFileSelect = useCallback((file: File | null) => {
    selectedFileRef.current = file;
  }, []);

  const schema = isEditMode ? UpdateProductSchema : CreateProductSchema;

  const brandOptions = [
    { id: "", name: "Chọn nhãn hàng" },
    ...allBrands.map(b => ({ id: String(b.id), name: b.name })),
  ];

  // Get default category IDs for edit mode
  const getDefaultCategoryIds = (): number[] => {
    if (!initialData?.categories) return [];
    return initialData.categories.map(c => c.categoryId);
  };

  const {
    register,
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<CreateProductValues>({
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

  // When categories are loaded, set the default category IDs for edit mode
  useEffect(() => {
    if (allCategories.length > 0 && initialData?.categories) {
      const categoryIds = getDefaultCategoryIds();
      setValue("categoryIds", categoryIds);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [allCategories]);

  const onSubmit = async (data: CreateProductValues) => {
    try {
      setIsLoading(true);

      // Upload thumbnail to Cloudinary if a new file is selected
      let thumbnailUrl = initialData?.thumbnail || "";
      if (selectedFileRef.current) {
        thumbnailUrl = await uploadToCloudinary(selectedFileRef.current);
      }

      // Upload all new images to Cloudinary
      const uploadedImages: { id?: number; imageUrl: string; displayOrder: number }[] = [];
      for (const img of imageRows) {
        if (img.file) {
          const url = await uploadToCloudinary(img.file);
          uploadedImages.push({ id: img.id, imageUrl: url, displayOrder: img.displayOrder });
        } else if (img.imageUrl) {
          uploadedImages.push({ id: img.id, imageUrl: img.imageUrl, displayOrder: img.displayOrder });
        }
      }

      const payload = {
        name: data.name!,
        slug: data.slug || data.name!.toLowerCase().replace(/\s+/g, "-"),
        productCode: data.productCode,
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
        attributes: attributeRows.filter(a => a.attributeId && a.value).map(a => ({
          ...(a.id ? { id: a.id } : {}),
          attributeId: Number(a.attributeId),
          value: a.value,
        })),
        images: uploadedImages,
      };

      if (isEditMode) {
        await productService.update(initialData.id, payload);
        toast.success("Cập nhật sản phẩm thành công!");
      } else {
        await productService.create(payload);
        toast.success("Tạo sản phẩm mới thành công!");
      }

      router.push("/products");
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
        onSubmit={(e) => handleSubmit(onSubmit as any)(e).catch(() => { })}
      >
        {/* Row 1: Tên + Slug */}
        <FormInput
          label="Tên sản phẩm"
          id="name"
          {...register("name")}
          error={errors.name?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Đường dẫn tĩnh (Slug)"
          id="slug"
          placeholder="Tự động tạo nếu để trống"
          {...register("slug")}
          error={errors.slug?.message}
          disabled={isReadOnly}
        />

        {/* Row 2: Mã SP + Nhãn hàng */}
        <FormInput
          label="Mã sản phẩm"
          id="productCode"
          {...register("productCode")}
          error={errors.productCode?.message}
          disabled={isReadOnly}
        />

        <Controller
          control={control}
          name="brandId"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="brandId"
              >
                Nhãn hàng
              </label>
              {isReadOnly ? (
                <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] px-[22px] font-[500] text-[14px] text-[var(--color-text)] w-full h-[52px] flex items-center opacity-60 cursor-not-allowed">
                  {initialData?.brandName || "Không có"}
                </div>
              ) : (
                <CustomSelect
                  options={brandOptions}
                  value={field.value ? String(field.value) : ""}
                  onChange={(val) => field.onChange(val ? Number(val) : 0)}
                  placeholder="Chọn nhãn hàng"
                  classNameCustom={`
                    bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                    px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                    w-full h-[52px] !block
                  `}
                />
              )}
              {errors.brandId?.message && (
                <p className="text-red-500 text-[13px] mt-[6px]">{errors.brandId.message}</p>
              )}
            </div>
          )}
        />

        {/* Row 3: Giá bán + Giá cũ */}
        <FormInput
          label="Giá bán"
          id="price"
          type="number"
          {...register("price")}
          error={errors.price?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Giá cũ"
          id="oldPrice"
          type="number"
          {...register("oldPrice")}
          error={errors.oldPrice?.message}
          disabled={isReadOnly}
        />

        {/* Row 4: Bảo hành + Trạng thái */}
        <FormInput
          label="Bảo hành (tháng)"
          id="warrantyMonths"
          type="number"
          {...register("warrantyMonths")}
          error={errors.warrantyMonths?.message}
          disabled={isReadOnly}
        />

        <FormControlledSelect
          control={control}
          name="status"
          label="Trạng thái"
          options={STATUS_OPTIONS}
          placeholder="Chọn trạng thái"
          disabled={isReadOnly}
        />

        {/* Row 5: Mô tả ngắn */}
        <FormInput
          label="Mô tả ngắn"
          id="shortDescription"
          {...register("shortDescription")}
          error={errors.shortDescription?.message}
          disabled={isReadOnly}
        />

        {/* SEO fields */}
        <FormInput
          label="Tiêu đề SEO (Meta Title)"
          id="metaTitle"
          {...register("metaTitle")}
          error={errors.metaTitle?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Mô tả SEO (Meta Description)"
          id="metaDescription"
          {...register("metaDescription")}
          error={errors.metaDescription?.message}
          disabled={isReadOnly}
        />

        {/* Category selection - full width */}
        <ProductCategoriesSection
          control={control}
          allCategories={allCategories}
          isReadOnly={isReadOnly}
          initialCategories={initialData?.categories}
          errors={errors}
        />

        {/* Attributes section - full width */}
        <ProductAttributesSection
          attributeRows={attributeRows}
          onAttributeRowsChange={setAttributeRows}
          allAttributes={allAttributes}
          isReadOnly={isReadOnly}
          initialAttributes={initialData?.attributes}
        />

        {/* Images section - full width */}
        <ProductImagesSection
          imageRows={imageRows}
          onImageRowsChange={setImageRows}
          isReadOnly={isReadOnly}
          initialImages={initialData?.images}
        />

        {/* Thumbnail upload */}
        <FormImageField
          label="Ảnh thumbnail"
          id="thumbnail"
          initialUrl={initialData?.thumbnail}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có ảnh"
          onFileSelect={handleImageFileSelect}
        />

        {/* Mô tả chi tiết - TinyMCE - full width */}
        <div className="md:col-span-2">
          <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
            Mô tả chi tiết
          </label>
          {isReadOnly ? (
            <div
              className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] prose prose-sm max-w-none text-[14px] text-[var(--color-text)]"
              dangerouslySetInnerHTML={{ __html: initialData?.description || '<p class="text-gray-400 italic">Chưa có mô tả</p>' }}
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

        {/* Submit button */}
        {!isReadOnly && (
          <FormButton type="submit" disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={isReadOnly ? "/products/trash" : "/products"} />
    </div>
  );
}
