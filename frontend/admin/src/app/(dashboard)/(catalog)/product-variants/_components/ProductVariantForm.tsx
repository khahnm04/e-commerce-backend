"use client";
import React, { useState, useEffect, useCallback } from "react";
import { useRouter } from "next/navigation";
import { toast } from "react-hot-toast";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { FormInput } from "@/components/form/FormInput";
import { CustomSelect } from "@/components/common/CustomSelect";
import { FormButton } from "@/components/form/FormButton";
import { FormBackLink } from "@/components/form/FormBackLink";
import {
  CreateProductVariantSchema,
  CreateProductVariantValues,
} from "@/app/(dashboard)/(catalog)/product-variants/product-variant.schema";
import { productVariantService } from "@/services/catalog/productVariant.service";
import { productService } from "@/services/catalog/product.service";
import { ProductVariantResponse } from "@/types/catalog/product-variant.types";
import { ProductResponse } from "@/types/catalog/product.types";

interface ProductVariantFormProps {
  initialData?: ProductVariantResponse;
}

export default function ProductVariantForm({ initialData }: ProductVariantFormProps) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [products, setProducts] = useState<ProductResponse[]>([]);
  const isEditMode = !!initialData;

  const loadProducts = useCallback(async () => {
    try {
      const res = await productService.getAll();
      setProducts(res.data ?? []);
    } catch {
      console.error("Không thể tải danh sách sản phẩm");
    }
  }, []);

  useEffect(() => {
    loadProducts();
  }, [loadProducts]);

  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateProductVariantValues>({
    resolver: zodResolver(CreateProductVariantSchema),
    defaultValues: {
      productId: initialData?.productId || 0,
      name: initialData?.name || "",
      sku: initialData?.sku || "",
      price: initialData?.price || 0,
      oldPrice: initialData?.oldPrice || 0,
      image: initialData?.image || "",
    },
  });

  const onSubmit = async (data: CreateProductVariantValues) => {
    try {
      setIsLoading(true);

      const payload = {
        productId: Number(data.productId),
        name: data.name,
        sku: data.sku,
        price: data.price,
        oldPrice: data.oldPrice,
        image: data.image,
      };

      if (isEditMode) {
        await productVariantService.update(initialData.id, payload);
        toast.success("Cập nhật biến thể sản phẩm thành công!");
      } else {
        await productVariantService.create(payload);
        toast.success("Tạo biến thể sản phẩm mới thành công!");
      }

      router.push("/product-variants");
      router.refresh();
    } catch (error: unknown) {
      console.error(error);
      toast.error(error instanceof Error ? error.message : "Có lỗi xảy ra, vui lòng thử lại!");
    } finally {
      setIsLoading(false);
    }
  };

  const productOptions = products.map((p) => ({
    id: String(p.id),
    name: p.name,
  }));

  return (
    <div className="bg-white border border-[#B9B9B9] rounded-[14px] md:p-[50px] p-[20px]">
      <form
        className="grid md:grid-cols-2 grid-cols-1 gap-[30px]"
        onSubmit={handleSubmit(onSubmit)}
      >
        <Controller
          control={control}
          name="productId"
          render={({ field }) => (
            <div className="inner-group relative">
              <label
                className="inner-label block font-[600] text-[14px] text-[#606060] mb-[11px]"
                htmlFor="productId"
              >
                Sản phẩm
              </label>
              <CustomSelect
                options={productOptions}
                value={String(field.value) ?? ""}
                onChange={(val) => field.onChange(Number(val))}
                placeholder="Chọn sản phẩm"
                classNameCustom={`
                  bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px]
                  px-[22px] font-[500] text-[14px] text-[var(--color-text)]
                  w-full h-[52px] !block
                `}
              />
              {errors.productId && (
                <span className="text-red-500 text-[13px] mt-[6px] block font-medium">
                  {errors.productId.message}
                </span>
              )}
            </div>
          )}
        />

        <FormInput
          label="Tên biến thể"
          id="name"
          {...register("name")}
          error={errors.name?.message}
        />

        <FormInput
          label="SKU"
          id="sku"
          placeholder="Tự động tạo nếu để trống"
          {...register("sku")}
          error={errors.sku?.message}
        />

        <FormInput
          label="Giá bán"
          id="price"
          type="number"
          {...register("price")}
          error={errors.price?.message}
        />

        <FormInput
          label="Giá cũ"
          id="oldPrice"
          type="number"
          {...register("oldPrice")}
          error={errors.oldPrice?.message}
        />

        <FormInput
          label="Ảnh URL"
          id="image"
          placeholder="https://..."
          {...register("image")}
          error={errors.image?.message}
        />

        <FormButton type="submit" disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : isEditMode ? "Cập nhật" : "Tạo mới"}
        </FormButton>
      </form>

      <FormBackLink href="/product-variants" />
    </div>
  );
}
