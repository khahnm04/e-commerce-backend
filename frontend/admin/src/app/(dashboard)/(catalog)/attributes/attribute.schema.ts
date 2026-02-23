import { z } from "zod";

const AttributeBase = z.object({
    name: z.string().min(1, "Tên thuộc tính không được để trống"),
    code: z.string().min(1, "Mã thuộc tính không được để trống"),
    description: z.string().optional(),
});

export const CreateAttributeSchema = AttributeBase;

export const UpdateAttributeSchema = AttributeBase.partial().extend({
    id: z.number().or(z.string()),
});

export type CreateAttributeValues = z.infer<typeof CreateAttributeSchema>;
export type UpdateAttributeValues = z.infer<typeof UpdateAttributeSchema>;
