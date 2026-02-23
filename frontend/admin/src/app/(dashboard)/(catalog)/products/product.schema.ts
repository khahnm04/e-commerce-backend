import { z } from "zod";

const ProductBase = z.object({
  name: z.string().min(1, "Tên sản phẩm không được để trống"),
  price: z.coerce.number().min(0, "Giá bán phải lớn hơn hoặc bằng 0"),
  old_price: z.coerce.number().min(0, "Giá cũ phải lớn hơn hoặc bằng 0").optional().nullable(),
  status: z.enum(['ACTIVE', 'INACTIVE', 'OUT_OF_STOCK', 'PENDING', 'DISCONTINUED']).default('ACTIVE'),
  brand_id: z.string().optional().nullable(),
  description: z.string().min(1, "Mô tả không được để trống"),
  thumbnail: z.any().optional(), // FilePond avatar
});

export const CreateProductSchema = ProductBase;

export const UpdateProductSchema = ProductBase.partial().extend({
  id: z.number().or(z.string()),
});

export type CreateProductValues = z.infer<typeof CreateProductSchema>;
export type UpdateProductValues = z.infer<typeof UpdateProductSchema>;
