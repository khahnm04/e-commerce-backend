import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { RoleResponse, RoleRequest, RoleFilterRequest, RoleBulkActionRequest } from "@/types/auth/role.types";

const BASE_ENDPOINT = "/admin/roles";

const buildQuery = (filter?: Record<string, unknown>): string => {
    if (!filter) return "";
    const params = new URLSearchParams();
    Object.entries(filter).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") params.append(key, String(value));
    });
    const q = params.toString();
    return q ? `?${q}` : "";
};

export const roleService = {
    getAll: (filter?: RoleFilterRequest) =>
        sendRequest<ApiResponse<RoleResponse[]>>(`${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

    getAllDeleted: (filter?: RoleFilterRequest) =>
        sendRequest<ApiResponse<RoleResponse[]>>(`${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

    getById: (id: number | string) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "GET", credentials: "include" }),

    getDeletedById: (id: number | string) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}/deleted/${id}`, { method: "GET", credentials: "include" }),

    create: (payload: RoleRequest) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}`, { method: "POST", body: JSON.stringify(payload), credentials: "include" }),

    update: (id: number | string, payload: RoleRequest) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "PUT", body: JSON.stringify(payload), credentials: "include" }),

    delete: (id: number | string) =>
        sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),

    restore: (id: number | string) =>
        sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/restore`, { method: "PATCH", credentials: "include" }),

    bulkAction: (payload: RoleBulkActionRequest) =>
        sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/bulk`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),
};
