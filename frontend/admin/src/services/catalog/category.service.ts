import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { CategoryResponse, CategoryRequest, CategoryFilterRequest, CategoryBulkActionRequest } from "@/types/catalog/category.types";

const BASE_ENDPOINT = "/admin/categories";

const buildQuery = (filter?: Record<string, unknown>): string => {
  if (!filter) return "";
  const params = new URLSearchParams();
  Object.entries(filter).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") params.append(key, String(value));
  });
  const q = params.toString();
  return q ? `?${q}` : "";
};

export const categoryService = {
  getAll: (filter?: CategoryFilterRequest) =>
    sendRequest<ApiResponse<CategoryResponse[]>>(`${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getAllDeleted: (filter?: CategoryFilterRequest) =>
    sendRequest<ApiResponse<CategoryResponse[]>>(`${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getById: (slug: string) =>
    sendRequest<ApiResponse<CategoryResponse>>(`${BASE_ENDPOINT}/slug/${slug}`, { method: "GET", credentials: "include" }),

  getDeletedById: (id: number | string) =>
    sendRequest<ApiResponse<CategoryResponse>>(`${BASE_ENDPOINT}/deleted/${id}`, { method: "GET", credentials: "include" }),

  create: (payload: CategoryRequest) =>
    sendRequest<ApiResponse<CategoryResponse>>(`${BASE_ENDPOINT}`, { method: "POST", body: JSON.stringify(payload), credentials: "include" }),

  update: (id: number | string, payload: CategoryRequest) =>
    sendRequest<ApiResponse<CategoryResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "PUT", body: JSON.stringify(payload), credentials: "include" }),

  updateStatus: (id: number | string, payload: { status: string }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),

  softDelete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/restore`, { method: "PATCH", credentials: "include" }),

  bulkAction: (payload: CategoryBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/bulk`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),
};
