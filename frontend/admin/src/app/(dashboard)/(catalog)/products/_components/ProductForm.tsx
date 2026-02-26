"use client";
import React from "react";
import { usePathname } from "next/navigation";
import { FormInput } from "@/components/form/FormInput";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import { FormControlledSelect } from "@/components/form/FormControlledSelect";
import { FormImageField } from "@/components/form/FormImageField";
import { FormReadOnlySelect } from "@/components/form/FormReadOnlySelect";
import { getFormOptions } from "@/constants/common.constants";
import { PRODUCT_STATUS_OPTIONS } from "@/constants/catalog/product.constants";
import { ProductAttributesSection } from "./ProductAttributesSection";
import { ProductImagesSection } from "./ProductImagesSection";
import { CheckboxGroupField } from "@/components/form/CheckboxGroupField";
import { ProductDescriptionSection } from "./ProductDescriptionSection";
import { useProductForm } from "./useProductForm";
import { ProductResponse } from "@/types/catalog/product.types";

const STATUS_OPTIONS = getFormOptions(PRODUCT_STATUS_OPTIONS);

interface ProductFormProps {
  initialData?: ProductResponse;
  isReadOnly?: boolean;
}

export default function ProductForm({ initialData, isReadOnly = false }: ProductFormProps) {
  const pathname = usePathname();
  const {
    formMethods: {
      register,
      control,
      handleSubmit,
      formState: { errors },
    },
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
  } = useProductForm(initialData, isReadOnly);

  const brandOptions = [
    { id: "", name: "Chọn nhãn hàng" },
    ...allBrands.map((b) => ({ id: String(b.id), name: b.name })),
  ];

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={(e) =>
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          handleSubmit(onSubmit as any, (validationErrors) =>
            console.error("Form validation errors:", validationErrors),
          )(e).catch(() => { })
        }
      >
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

        <FormInput
          label="Mã sản phẩm"
          id="productCode"
          {...register("productCode")}
          error={errors.productCode?.message}
          disabled={isReadOnly}
        />

        <FormReadOnlySelect
          control={control}
          name="brandId"
          label="Nhãn hàng"
          options={brandOptions}
          placeholder="Chọn nhãn hàng"
          readOnlyText={initialData?.brandName || "Không có"}
          isReadOnly={isReadOnly}
          error={errors.brandId?.message}
          emptyValue={0}
        />

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

        <FormInput
          label="Mô tả ngắn"
          id="shortDescription"
          {...register("shortDescription")}
          error={errors.shortDescription?.message}
          disabled={isReadOnly}
        />

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

        <CheckboxGroupField
          control={control}
          name="categoryIds"
          label="Danh mục"
          items={allCategories}
          isReadOnly={isReadOnly}
          readOnlyLabels={initialData?.categories?.map((c) => c.categoryName)}
          emptyReadOnlyText="Chưa gán danh mục"
          loadingText="Đang tải danh sách danh mục..."
          errors={errors}
        />

        <ProductAttributesSection
          attributeRows={attributeRows}
          onAttributeRowsChange={setAttributeRows}
          allAttributes={allAttributes}
          isReadOnly={isReadOnly}
          initialAttributes={initialData?.attributes}
        />

        <ProductImagesSection
          imageRows={imageRows}
          onImageRowsChange={setImageRows}
          isReadOnly={isReadOnly}
          initialImages={initialData?.images}
        />

        <FormImageField
          label="Ảnh thumbnail"
          id="thumbnail"
          initialUrl={initialData?.thumbnail}
          isReadOnly={isReadOnly}
          isEditMode={isEditMode}
          noImageText="Không có ảnh"
          onFileSelect={handleImageFileSelect}
        />

        <ProductDescriptionSection
          control={control}
          isReadOnly={isReadOnly}
          initialDescription={initialData?.description}
        />

        {!isReadOnly && (
          <FormButton
            type="submit"
            disabled={isLoading}
          >
            {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
          </FormButton>
        )}
      </form>

      <FormBackLink href={pathname.includes("/trash") ? "/products/trash" : "/products"} />
    </div>
  );
}
