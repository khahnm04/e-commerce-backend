import { ProductStatus } from "@/constants/enums";
import { BaseEntity } from "../common/base.types";

export interface ProductAttributeValueResponse {
  id: number;
  attributeId: number;
  attributeName: string;
  value: string;
}

export interface ProductCategoryResponse {
  id: number;
  categoryId: number;
  categoryName: string;
}

export interface ProductImageResponse {
  id: number;
  imageUrl: string;
  displayOrder: number;
}

export interface ProductResponse extends BaseEntity {
  name: string;
  slug: string;
  productCode: string;
  brandName: string;
  brandId: number;
  hasSerialNumber: boolean;
  warrantyMonths: number;
  price: number;
  oldPrice: number;
  thumbnail: string;
  description: string;
  shortDescription: string;
  metaTitle: string;
  metaDescription: string;
  status: ProductStatus;
  attributes: ProductAttributeValueResponse[];
  categories: ProductCategoryResponse[];
  images: ProductImageResponse[];
}

export interface ProductAttributeValueRequest {
  id?: number;
  attributeId: number;
  value: string;
}

export interface ProductImageRequest {
  id?: number;
  imageUrl: string;
  displayOrder?: number;
}

export interface ProductRequest {
  name: string;
  slug: string;
  productCode?: string;
  brandId: number;
  hasSerialNumber?: boolean;
  warrantyMonths?: number;
  price: number;
  oldPrice?: number;
  thumbnail: string;
  description?: string;
  shortDescription?: string;
  metaTitle?: string;
  metaDescription?: string;
  status?: ProductStatus;
  categoryIds?: number[];
  attributes?: ProductAttributeValueRequest[];
  images?: ProductImageRequest[];
}

export interface ProductUpdateStatusRequest {
  status: ProductStatus;
}

export interface ProductFilterRequest {
  page?: number;
  size?: number;
  sort?: string;
  status?: ProductStatus;
  brandId?: number;
  categoryId?: number;
  keyword?: string;
  startDate?: string;
  endDate?: string;
}

export interface ProductBulkActionRequest {
  products: number[];
  action: string;
}
