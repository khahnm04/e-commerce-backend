import { BaseEntity } from "../common/base.types";

export interface VariantResponse extends BaseEntity {
    name: string;
    code: string;
}

export interface VariantRequest {
    name: string;
    code: string;
}

export interface VariantFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    keyword?: string;
    startDate?: string;
    endDate?: string;
}

export interface VariantBulkActionRequest {
    variants: number[];
    action: string;
}
