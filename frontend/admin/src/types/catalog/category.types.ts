import { CategoryStatus } from "@/constants/enums";
import { BaseEntity } from "../common/base.types";

export interface CategoryResponse extends BaseEntity {
  name: string;
  slug: string;
  description: string;
  iconUrl: string;
  bannerUrl: string;
  parentId: number | null;
  parentName: string;
  position: number;
  level: number;
  path: string;
  metaTitle: string;
  metaDescription: string;
  metaKeywords: string;
  status: CategoryStatus;
}

export interface CategoryRequest {
  name: string;
  slug: string;
  description?: string;
  iconUrl?: string;
  bannerUrl?: string;
  position?: number;
  metaTitle?: string;
  metaDescription?: string;
  metaKeywords?: string;
  status?: CategoryStatus;
  parentId?: number | null;
}

export interface CategoryFilterRequest {
  page?: number;
  size?: number;
  sort?: string;
  status?: CategoryStatus;
  parentId?: number;
  keyword?: string;
  dateType?: string;
  startDate?: string;
  endDate?: string;
}

export interface CategoryBulkActionRequest {
  categories: number[];
  action: string;
}
