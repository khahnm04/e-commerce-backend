import { sendRequest } from "@/lib/api-client";
import { buildQuery } from "@/lib/query-builder";
import { ApiResponse } from "@/types/common/api.types";
import {
  RoleResponse,
  RoleRequest,
  RoleFilterRequest,
  RoleBulkActionRequest,
} from "@/types/auth/role.types";

const BASE_ENDPOINT = "/admin/roles";

export const roleService = {
  getAll: (filter?: RoleFilterRequest) =>
    sendRequest<ApiResponse<RoleResponse[]>>(
      `${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getAllDeleted: (filter?: RoleFilterRequest) =>
    sendRequest<ApiResponse<RoleResponse[]>>(
      `${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<RoleResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "GET" }
    ),

  getDeletedById: (id: number | string) =>
    sendRequest<ApiResponse<RoleResponse>>(
      `${BASE_ENDPOINT}/deleted/${id}`,
      { method: "GET" }
    ),

  create: (payload: RoleRequest) =>
    sendRequest<ApiResponse<RoleResponse>>(
      `${BASE_ENDPOINT}`,
      { method: "POST", body: JSON.stringify(payload) }
    ),

  update: (id: number | string, payload: RoleRequest) =>
    sendRequest<ApiResponse<RoleResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "PUT", body: JSON.stringify(payload) }
    ),

  delete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "DELETE" }
    ),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/${id}/restore`,
      { method: "PATCH" }
    ),

  bulkAction: (payload: RoleBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/bulk`,
      { method: "PATCH", body: JSON.stringify(payload) }
    ),
};
