import { z } from "zod";

const CategoryBase = z.object({
  name: z.string().min(1, "Tên danh mục không được để trống"),
  position: z.coerce.number().min(0, "Vị trí phải là số dương"),
  status: z.enum(["active", "inactive"]).optional(),
  slug: z.string().optional(),
  parent: z.string().optional(),
  avatar: z.any().optional(),
});

export const CreateCategorySchema = CategoryBase.extend({
});

export const UpdateCategorySchema = CategoryBase.partial().extend({
  id: z.number().or(z.string()),
});

export type CreateCategoryValues = z.infer<typeof CreateCategorySchema>;
export type UpdateCategoryValues = z.infer<typeof UpdateCategorySchema>;
