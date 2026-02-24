import { BaseEntity } from "../common/base.types";

export interface AttributeResponse extends BaseEntity {
    name: string;
    code: string;
    description: string;
}

export interface AttributeRequest {
    name: string;
    code: string;
    description?: string;
}

export interface AttributeFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    keyword?: string;
    startDate?: string;
    endDate?: string;
}

export interface AttributeBulkActionRequest {
    attributes: number[];
    action: string;
}
