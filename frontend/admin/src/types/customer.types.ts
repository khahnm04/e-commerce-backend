import { UserStatus, Gender } from "@/constants/enums";
import { BaseEntity } from "./base.types";

// === Response (matches CustomerResponse.java) ===

export interface CustomerResponse extends BaseEntity {
    id: number;
    userId: number;
    fullName: string;
    email: string;
    phoneNumber: string;
    dateOfBirth: string | null;
    gender: Gender;
    avatarUrl: string;
    status: UserStatus;
    addressCount: number;
}

// === Request (matches CustomerCreationRequest.java) ===

export interface CustomerCreationRequest {
    email: string;
    phoneNumber: string;
    password?: string;
    confirmPassword?: string;
    fullName: string;
    dateOfBirth?: string | null;
    avatarUrl: string;
    gender?: Gender;
    status?: UserStatus;
}

// === Request (matches CustomerUpdateRequest.java) ===

export interface CustomerUpdateRequest {
    email: string;
    phoneNumber: string;
    fullName: string;
    dateOfBirth?: string | null;
    avatarUrl?: string;
    gender?: Gender;
    status?: UserStatus;
}

// === Request (matches CustomerUpdateStatusRequest.java) ===

export interface CustomerUpdateStatusRequest {
    status: UserStatus;
}

// === Request (matches CustomerFilterRequest.java) ===

export interface CustomerFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    status?: UserStatus;
    keyword?: string;
    startDate?: string;
    endDate?: string;
}

// === Request (matches CustomerBulkActionRequest.java) ===

export interface CustomerBulkActionRequest {
    customers: number[];
    action: string;
}
