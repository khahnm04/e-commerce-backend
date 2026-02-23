import { z } from "zod";

const VariantBase = z.object({
    name: z.string().min(1, "Tên biến thể không được để trống"),
    code: z.string().min(1, "Mã biến thể không được để trống"),
});

export const CreateVariantSchema = VariantBase;

export const UpdateVariantSchema = VariantBase.partial().extend({
    id: z.number().or(z.string()),
});

export type CreateVariantValues = z.infer<typeof CreateVariantSchema>;
export type UpdateVariantValues = z.infer<typeof UpdateVariantSchema>;
