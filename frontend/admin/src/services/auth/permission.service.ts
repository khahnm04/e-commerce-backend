import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { PermissionResponse } from "@/types/auth/role.types";

const BASE_ENDPOINT = "/admin/permissions";

export const permissionService = {
    getAll: () =>
        sendRequest<ApiResponse<PermissionResponse[]>>(`${BASE_ENDPOINT}`, { method: "GET" }),
};
