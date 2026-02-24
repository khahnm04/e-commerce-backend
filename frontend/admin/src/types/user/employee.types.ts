import { UserStatus, Gender } from "@/constants/enums";
import { BaseEntity } from "../common/base.types";

// === Response (matches EmployeeResponse.java) ===

export interface EmployeeResponse extends BaseEntity {
    id: number;
    userId: number;
    code: string;
    fullName: string;
    email: string;
    phoneNumber: string;
    jobTitle: string;
    branch: string;
    dateOfBirth: string | null;
    gender: Gender;
    avatarUrl: string;
    status: UserStatus;
    roles: string[];
}

// === Request (matches EmployeeCreationRequest.java) ===

export interface EmployeeCreationRequest {
    email: string;
    phoneNumber: string;
    password?: string;
    confirmPassword?: string;
    code: string;
    fullName: string;
    branchId: number;
    jobTitle?: string;
    dateOfBirth?: string | null;
    avatarUrl: string;
    gender?: Gender;
    status?: UserStatus;
    roles: number[];
}

// === Request (matches EmployeeUpdateRequest.java) ===

export interface EmployeeUpdateRequest {
    email: string;
    phoneNumber: string;
    code: string;
    fullName: string;
    branchId: number;
    jobTitle?: string;
    dateOfBirth?: string | null;
    avatarUrl: string;
    gender?: Gender;
    status?: UserStatus;
    roles: number[];
}

// === Request (matches EmployeeUpdateStatusRequest.java) ===

export interface EmployeeUpdateStatusRequest {
    status: UserStatus;
}

// === Request (matches EmployeeFilterRequest.java) ===

export interface EmployeeFilterRequest {
    page?: number;
    size?: number;
    sort?: string;
    status?: UserStatus;
    keyword?: string;
    branchId?: number;
    startDate?: string;
    endDate?: string;
}

// === Request (matches EmployeeBulkActionRequest.java) ===

export interface EmployeeBulkActionRequest {
    employees: number[];
    action: string;
}
