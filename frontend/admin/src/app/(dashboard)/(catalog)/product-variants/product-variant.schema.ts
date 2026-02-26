import { z } from "zod";

const ProductVariantBase = z.object({
  productId: z.number().min(1, "Vui lòng chọn sản phẩm"),
  name: z.string().min(1, "Tên biến thể sản phẩm không được để trống"),
  sku: z.string().optional(),
  price: z.number().min(0, "Giá phải lớn hơn hoặc bằng 0"),
  oldPrice: z.number().min(0, "Giá cũ phải lớn hơn hoặc bằng 0").optional(),
  image: z.string().optional(),
  variantValueIds: z.array(z.number()).optional(),
});

export const CreateProductVariantSchema = ProductVariantBase;

export const UpdateProductVariantSchema = ProductVariantBase.partial();

export type CreateProductVariantValues = z.infer<typeof CreateProductVariantSchema>;
export type UpdateProductVariantValues = z.infer<typeof UpdateProductVariantSchema>;
