import { z } from "zod";

const BrandBase = z.object({
  name: z.string().min(1, "Tên nhãn hàng không được để trống"),
  slug: z.string().optional(),
  description: z.string().optional(),
  logoUrl: z.string().optional(),
  country: z.string().optional(),
  websiteUrl: z.string().optional(),
  metaTitle: z.string().optional(),
  metaDescription: z.string().optional(),
  metaKeywords: z.string().optional(),
  status: z.enum(["ACTIVE", "INACTIVE"]).default("ACTIVE"),
});

export const CreateBrandSchema = BrandBase;

export const UpdateBrandSchema = BrandBase.partial().extend({
  id: z.number().or(z.string()),
});

export type CreateBrandValues = z.infer<typeof CreateBrandSchema>;
export type UpdateBrandValues = z.infer<typeof UpdateBrandSchema>;
