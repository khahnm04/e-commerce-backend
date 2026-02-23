import { BaseEntity } from "./base.types";

export interface VariantValueResponse extends BaseEntity {
    variantId: number;
    variantName: string;
    code: string;
    value: string;
    displayOrder: number;
}

export interface VariantValueRequest {
    variantId: number;
    code?: string;
    value: string;
    displayOrder?: number;
}

export interface VariantValueFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    variantId?: number;
    keyword?: string;
    startDate?: string;
    endDate?: string;
}

export interface VariantValueBulkActionRequest {
    variantValues: number[];
    action: string;
}
