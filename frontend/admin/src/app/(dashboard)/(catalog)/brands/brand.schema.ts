import { z } from "zod";

const BrandBase = z.object({
  name: z.string().min(1, "Tên nhãn hàng không được để trống"),
  slug: z.string().optional().default(""),
  description: z.string().optional().default(""),
  logoUrl: z.string().optional().default(""),
  country: z.string().optional().default(""),
  websiteUrl: z.string().optional().default(""),
  metaTitle: z.string().optional().default(""),
  metaDescription: z.string().optional().default(""),
  metaKeywords: z.string().optional().default(""),
  status: z.enum(["ACTIVE", "INACTIVE"]).default("ACTIVE"),
});

export const CreateBrandSchema = BrandBase;

export const UpdateBrandSchema = BrandBase.partial();

export type CreateBrandValues = z.infer<typeof CreateBrandSchema>;
export type UpdateBrandValues = z.infer<typeof UpdateBrandSchema>;
