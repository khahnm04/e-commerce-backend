import { ProductVariantStatus } from "@/constants/enums";
import { BaseEntity } from "./base.types";

export interface VariantValueInfo {
    id: number;
    variantName: string;
    value: string;
    code: string;
}

export interface ProductVariantResponse extends BaseEntity {
    productId: number;
    productName: string;
    sku: string;
    name: string;
    price: number;
    oldPrice: number;
    image: string;
    status: ProductVariantStatus;
    variantValues: VariantValueInfo[];
}

export interface ProductVariantRequest {
    productId: number;
    sku?: string;
    name: string;
    price: number;
    oldPrice?: number;
    image?: string;
    variantValueIds?: number[];
}

export interface ProductVariantUpdateStatusRequest {
    status: ProductVariantStatus;
}

export interface ProductVariantFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    productId?: number;
    status?: ProductVariantStatus;
    keyword?: string;
    startDate?: string;
    endDate?: string;
}

export interface ProductVariantBulkActionRequest {
    productVariants: number[];
    action: string;
}
