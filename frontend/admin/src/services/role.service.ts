import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/api.types";
import { RoleResponse, RoleRequest } from "@/types/role.types";

const BASE_ENDPOINT = "/admin/roles";

export const roleService = {
    getAll: () =>
        sendRequest<ApiResponse<RoleResponse[]>>(`${BASE_ENDPOINT}`, { method: "GET", credentials: "include" }),

    getById: (id: number | string) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "GET", credentials: "include" }),

    create: (payload: RoleRequest) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}`, { method: "POST", body: JSON.stringify(payload), credentials: "include" }),

    update: (id: number | string, payload: RoleRequest) =>
        sendRequest<ApiResponse<RoleResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "PUT", body: JSON.stringify(payload), credentials: "include" }),

    delete: (id: number | string) =>
        sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),
};
