import { z } from "zod";

const VariantValueBase = z.object({
  variantId: z.number().min(1, "Vui lòng chọn biến thể"),
  value: z.string().min(1, "Giá trị không được để trống"),
  code: z.string().optional(),
  displayOrder: z.number().min(0, "Thứ tự phải là số dương").optional(),
});

export const CreateVariantValueSchema = VariantValueBase;

export const UpdateVariantValueSchema = VariantValueBase.partial().extend({
  id: z.number().or(z.string()),
});

export type CreateVariantValueValues = z.infer<typeof CreateVariantValueSchema>;
export type UpdateVariantValueValues = z.infer<typeof UpdateVariantValueSchema>;
