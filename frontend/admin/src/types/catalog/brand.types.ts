import { BrandStatus } from "@/constants/enums";
import { BaseEntity } from "../common/base.types";

export interface BrandResponse extends BaseEntity {
  name: string;
  slug: string;
  description: string;
  logoUrl: string;
  country: string;
  websiteUrl: string;
  metaTitle: string;
  metaDescription: string;
  metaKeywords: string;
  status: BrandStatus;
}

export interface BrandRequest {
  name: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  country?: string;
  websiteUrl?: string;
  metaTitle?: string;
  metaDescription?: string;
  metaKeywords?: string;
  status?: BrandStatus;
}

export interface BrandUpdateStatusRequest {
  status: BrandStatus;
}

export interface BrandFilterRequest {
  page?: number;
  size?: number;
  sort?: string;
  status?: BrandStatus;
  keyword?: string;
  dateType?: string;
  startDate?: string;
  endDate?: string;
}

export interface BrandBulkActionRequest {
  brands: number[];
  action: string;
}
