import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/api.types";
import { PermissionResponse } from "@/types/role.types";

const BASE_ENDPOINT = "/admin/permissions";

export const permissionService = {
    getAll: () =>
        sendRequest<ApiResponse<PermissionResponse[]>>(`${BASE_ENDPOINT}`, { method: "GET", credentials: "include" }),
};
