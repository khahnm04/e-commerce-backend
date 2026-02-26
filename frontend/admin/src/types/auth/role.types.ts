export interface PermissionResponse {
  id: number;
  code: string;
  name: string;
  module: string;
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
  createdBy: string;
  updatedAt: string;
  updatedBy: string;
  deletedAt?: string;
}

export interface RoleRequest {
  code: string;
  name: string;
  description: string;
  permissions: number[]; // Set of Permission IDs
}

export interface RoleFilterRequest {
  page?: number;
  size?: number;
  sort?: string;
  keyword?: string;
}

export interface RoleBulkActionRequest {
  ids: number[];
  action: string;
}
