/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/ban-ts-comment */
// @ts-nocheck
"use client";
import React, { useState, useCallback, useEffect } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import "filepond/dist/filepond.min.css";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";
import { useFilePond } from "@/hooks/useFilePond";
import { FormInput } from "@/components/form/FormInput";
import { FormImageUpload } from "@/components/form/FormImageUpload";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
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
import { BrandResponse } from "@/types/catalog/brand.types";
import { CategoryResponse } from "@/types/catalog/category.types";
import { uploadToCloudinary } from "@/services/upload.service";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "OUT_OF_STOCK", name: "Hết hàng" },
  { id: "PENDING", name: "Chờ duyệt" },
  { id: "DISCONTINUED", name: "Ngừng bán" },
];

interface ProductFormProps {
  initialData?: ProductResponse;
  isReadOnly?: boolean;
}

export default function ProductForm({ initialData, isReadOnly = false }: ProductFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string>(initialData?.thumbnail || "");
  const [allBrands, setAllBrands] = useState<BrandResponse[]>([]);
  const [allCategories, setAllCategories] = useState<CategoryResponse[]>([]);
  const isEditMode = !!initialData;

  // Fetch brands
  useEffect(() => {
    const fetchBrands = async () => {
      try {
        const res = await brandService.getAll({ size: 99 });
        setAllBrands(res.data ?? []);
      } catch {
        // ignore
      }
    };
    fetchBrands();
  }, []);

  // Fetch categories
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await categoryService.getAll({ size: 99 });
        setAllCategories(res.data ?? []);
      } catch {
        // ignore
      }
    };
    fetchCategories();
  }, []);

  const handleAddFile = useCallback((file: File) => {
    setSelectedFile(file);
    setPreviewUrl(URL.createObjectURL(file));
  }, []);

  const handleRemoveFile = useCallback(() => {
    setSelectedFile(null);
    setPreviewUrl(isEditMode ? (initialData?.thumbnail || "") : "");
  }, [isEditMode, initialData?.thumbnail]);

  useFilePond({
    onAddFile: handleAddFile,
    onRemoveFile: handleRemoveFile,
  });

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
  } = useForm<CreateProductValues | UpdateProductValues>({
    resolver: zodResolver(schema),
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

  const onSubmit = async (data: CreateProductValues | UpdateProductValues) => {
    try {
      setIsLoading(true);

      // Upload thumbnail to Cloudinary if a new file is selected
      let thumbnailUrl = initialData?.thumbnail || "";
      if (selectedFile) {
        thumbnailUrl = await uploadToCloudinary(selectedFile);
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
        status: data.status,
        categoryIds: data.categoryIds || [],
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
        onSubmit={(e) => handleSubmit(onSubmit)(e).catch(() => { })}
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
          label="Slug"
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
              <CustomSelect
                options={brandOptions}
                value={field.value ? String(field.value) : ""}
                onChange={(val) => field.onChange(val ? Number(val) : 0)}
                placeholder="Chọn nhãn hàng"
                disabled={isReadOnly}
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                  ${isReadOnly ? 'opacity-60 cursor-not-allowed' : ''}
                `}
              />
              {(errors as any).brandId?.message && (
                <p className="text-red-500 text-[13px] mt-[6px]">{(errors as any).brandId.message}</p>
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
          error={(errors as any).oldPrice?.message}
          disabled={isReadOnly}
        />

        {/* Row 4: Bảo hành + Trạng thái */}
        <FormInput
          label="Bảo hành (tháng)"
          id="warrantyMonths"
          type="number"
          {...register("warrantyMonths")}
          error={(errors as any).warrantyMonths?.message}
          disabled={isReadOnly}
        />

        <Controller
          control={control}
          name="status"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="status"
              >
                Trạng thái
              </label>
              <CustomSelect
                options={STATUS_OPTIONS}
                value={field.value ?? ""}
                onChange={(val) => field.onChange(val)}
                placeholder="Chọn trạng thái"
                disabled={isReadOnly}
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                  ${isReadOnly ? 'opacity-60 cursor-not-allowed' : ''}
                `}
              />
            </div>
          )}
        />

        {/* Row 5: Mô tả ngắn */}
        <FormInput
          label="Mô tả ngắn"
          id="shortDescription"
          {...register("shortDescription")}
          error={(errors as any).shortDescription?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Mô tả chi tiết"
          id="description"
          {...register("description")}
          error={(errors as any).description?.message}
          disabled={isReadOnly}
        />

        {/* Row 6: Category selection - full width */}
        <Controller
          control={control}
          name="categoryIds"
          render={({ field }) => (
            <div className="md:col-span-2">
              <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
                Danh mục
              </label>
              {isReadOnly ? (
                <div className="flex flex-wrap gap-[8px]">
                  {initialData?.categories && initialData.categories.length > 0 ? (
                    initialData.categories.map((cat) => (
                      <span
                        key={cat.id}
                        className="bg-blue-50 text-blue-700 border border-blue-200 rounded-[6px] px-[12px] py-[6px] text-[13px] font-[600]"
                      >
                        {cat.categoryName}
                      </span>
                    ))
                  ) : (
                    <p className="text-gray-400 text-sm italic">Chưa gán danh mục</p>
                  )}
                </div>
              ) : (
                <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] flex flex-wrap gap-[12px]">
                  {allCategories.length === 0 ? (
                    <p className="text-gray-400 text-sm italic">Đang tải danh sách danh mục...</p>
                  ) : (
                    allCategories.map((cat) => {
                      const isChecked = (field.value || []).includes(cat.id);
                      return (
                        <label
                          key={cat.id}
                          className={`
                            inline-flex items-center gap-[8px] cursor-pointer
                            border rounded-[6px] px-[14px] py-[8px]
                            text-[13px] font-[600] transition-all
                            ${isChecked
                              ? "bg-blue-50 border-blue-400 text-blue-700"
                              : "bg-white border-[#D5D5D5] text-[#606060] hover:border-blue-300 hover:bg-blue-50/50"
                            }
                          `}
                        >
                          <input
                            type="checkbox"
                            checked={isChecked}
                            className="w-[16px] h-[16px] accent-blue-600 cursor-pointer"
                            onChange={(e) => {
                              const current = field.value || [];
                              if (e.target.checked) {
                                field.onChange([...current, cat.id]);
                              } else {
                                field.onChange(current.filter((id: number) => id !== cat.id));
                              }
                            }}
                          />
                          {cat.name}
                        </label>
                      );
                    })
                  )}
                </div>
              )}
              {(errors as any).categoryIds?.message && (
                <p className="text-red-500 text-[13px] mt-[6px]">{(errors as any).categoryIds.message}</p>
              )}
            </div>
          )}
        />

        {/* Row 7: Thumbnail upload */}
        {isReadOnly ? (
          <div className="md:col-span-2">
            <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
              Ảnh thumbnail
            </label>
            {initialData?.thumbnail ? (
              <img
                src={initialData.thumbnail}
                alt="Thumbnail"
                className="w-[150px] h-auto object-cover rounded border"
              />
            ) : (
              <p className="text-gray-400 text-sm italic">Không có ảnh</p>
            )}
          </div>
        ) : (
          <div className="md:col-span-2 flex items-center gap-[50px]">
            <FormImageUpload
              label="Ảnh thumbnail"
              id="thumbnail"
              name="thumbnail"
            />
            {previewUrl && (
              <div className="flex flex-col items-start gap-[11px] font-[600] text-[14px] text-[#606060] h-full">
                <span>{selectedFile ? "Ảnh mới:" : "Ảnh hiện tại:"}</span>
                <img
                  src={previewUrl}
                  alt="Thumbnail preview"
                  className="w-[150px] h-[150px] object-cover rounded-[8px] border border-[#D5D5D5]"
                />
              </div>
            )}
          </div>
        )}

        {/* SEO fields */}
        <FormInput
          label="Meta Title"
          id="metaTitle"
          {...register("metaTitle")}
          error={(errors as any).metaTitle?.message}
          disabled={isReadOnly}
        />

        <FormInput
          label="Meta Description"
          id="metaDescription"
          {...register("metaDescription")}
          error={(errors as any).metaDescription?.message}
          disabled={isReadOnly}
        />

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
