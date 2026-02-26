import { z } from "zod";

const ProductBase = z.object({
  name: z.string().min(1, "Tên sản phẩm không được để trống"),
  slug: z.string().optional().default(""),
  productCode: z.string().optional().default(""),
  brandId: z.coerce.number().min(1, "Vui lòng chọn nhãn hàng"),
  hasSerialNumber: z.boolean().optional().default(false),
  warrantyMonths: z.coerce.number().optional().default(0),
  price: z.coerce.number().min(0, "Giá bán phải lớn hơn hoặc bằng 0"),
  oldPrice: z.coerce.number().min(0, "Giá cũ phải lớn hơn hoặc bằng 0").optional().nullable(),
  thumbnail: z.string().optional().default(""),
  description: z.string().optional().default(""),
  shortDescription: z.string().optional().default(""),
  metaTitle: z.string().optional().default(""),
  metaDescription: z.string().optional().default(""),
  status: z
    .enum(["ACTIVE", "INACTIVE", "OUT_OF_STOCK", "PENDING", "DISCONTINUED"])
    .default("ACTIVE"),
  categoryIds: z.array(z.number()).optional().default([]),
  attributes: z
    .array(
      z.object({
        id: z.number().optional(),
        attributeId: z.number(),
        value: z.string().min(1, "Giá trị không được để trống"),
      }),
    )
    .optional()
    .default([]),
  images: z
    .array(
      z.object({
        id: z.number().optional(),
        imageUrl: z.string().min(1, "URL ảnh không được để trống"),
        displayOrder: z.number().optional().default(0),
      }),
    )
    .optional()
    .default([]),
});

export const CreateProductSchema = ProductBase;

export const UpdateProductSchema = ProductBase.partial().extend({
  id: z.number().or(z.string()),
});

export type CreateProductValues = z.infer<typeof CreateProductSchema>;
export type UpdateProductValues = z.infer<typeof UpdateProductSchema>;
