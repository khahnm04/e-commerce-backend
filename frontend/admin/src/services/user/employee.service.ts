import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { EmployeeResponse, EmployeeCreationRequest, EmployeeUpdateRequest, EmployeeUpdateStatusRequest, EmployeeFilterRequest, EmployeeBulkActionRequest } from "@/types/user/employee.types";

const BASE_ENDPOINT = "/admin/employees";

const buildQuery = (filter?: Record<string, unknown>): string => {
  if (!filter) return "";
  const params = new URLSearchParams();
  Object.entries(filter).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") params.append(key, String(value));
  });
  const q = params.toString();
  return q ? `?${q}` : "";
};

export const employeeService = {
  getAll: (filter?: EmployeeFilterRequest) =>
    sendRequest<ApiResponse<EmployeeResponse[]>>(`${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getAllDeleted: (filter?: EmployeeFilterRequest) =>
    sendRequest<ApiResponse<EmployeeResponse[]>>(`${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<EmployeeResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "GET", credentials: "include" }),

  create: (payload: EmployeeCreationRequest) =>
    sendRequest<ApiResponse<EmployeeResponse>>(`${BASE_ENDPOINT}`, { method: "POST", body: JSON.stringify(payload), credentials: "include" }),

  update: (id: number | string, payload: EmployeeUpdateRequest) =>
    sendRequest<ApiResponse<EmployeeResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "PUT", body: JSON.stringify(payload), credentials: "include" }),

  updateStatus: (id: number | string, payload: EmployeeUpdateStatusRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),

  softDelete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/restore`, { method: "PATCH", credentials: "include" }),

  bulkAction: (payload: EmployeeBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/bulk`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),
};
