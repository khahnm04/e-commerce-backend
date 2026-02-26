import { z } from "zod";

const CategoryBase = z.object({
  name: z.string().min(1, "Tên danh mục không được để trống"),
  slug: z.string().optional().default(""),
  description: z.string().optional().default(""),
  iconUrl: z.string().optional().default(""),
  bannerUrl: z.string().optional().default(""),
  position: z.coerce.number().min(0, "Vị trí phải là số dương").optional().default(0),
  metaTitle: z.string().optional().default(""),
  metaDescription: z.string().optional().default(""),
  metaKeywords: z.string().optional().default(""),
  status: z.enum(["ACTIVE", "INACTIVE"]).default("ACTIVE"),
  parentId: z.coerce.number().optional().nullable(),
});

export const CreateCategorySchema = CategoryBase;

export const UpdateCategorySchema = CategoryBase.partial();

export type CreateCategoryValues = z.infer<typeof CreateCategorySchema>;
export type UpdateCategoryValues = z.infer<typeof UpdateCategorySchema>;
