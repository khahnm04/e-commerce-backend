export interface PermissionResponse {
    id: number;
    code: string;
    name: string;
    description: string;
    createdAt: string;
}

export interface RoleResponse {
    id: number;
    code: string;
    name: string;
    description: string;
    permissions: string[];
    createdAt: string;
    updatedAt: string;
}

export interface RoleRequest {
    code: string;
    name: string;
    description: string;
    permissions: number[]; // Set of Permission IDs
}
