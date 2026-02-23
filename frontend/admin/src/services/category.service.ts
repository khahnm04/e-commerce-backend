import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/api.types";
import { Category, CategoryPayload } from "@/types/category.types";

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
  getAll: (filter?: Record<string, unknown>) =>
    sendRequest<ApiResponse<Category[]>>(`${BASE_ENDPOINT}${buildQuery(filter)}`, { method: "GET", credentials: "include" }),

  getAllDeleted: (filter?: Record<string, unknown>) =>
    sendRequest<ApiResponse<Category[]>>(`${BASE_ENDPOINT}/deleted${buildQuery(filter)}`, { method: "GET", credentials: "include" }),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<Category>>(`${BASE_ENDPOINT}/${id}`, { method: "GET", credentials: "include" }),

  create: (payload: CategoryPayload, imageFile: File) => {
    const formData = new FormData();
    const jsonBlob = new Blob([JSON.stringify(payload)], { type: "application/json" });
    formData.append("data", jsonBlob);
    formData.append("image", imageFile);
    return sendRequest<ApiResponse<Category>>(`${BASE_ENDPOINT}`, {
      method: "POST", body: formData, headers: { "Content-Type": undefined as unknown as string }, credentials: "include",
    });
  },

  update: (id: number | string, payload: CategoryPayload, imageFile?: File | null) => {
    const formData = new FormData();
    const jsonBlob = new Blob([JSON.stringify(payload)], { type: "application/json" });
    formData.append("data", jsonBlob);
    if (imageFile) formData.append("image", imageFile);
    return sendRequest<ApiResponse<Category>>(`${BASE_ENDPOINT}/${id}`, {
      method: "PUT", body: formData, headers: { "Content-Type": undefined as unknown as string }, credentials: "include",
    });
  },

  updateStatus: (id: number | string, payload: { status: string }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),

  softDelete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/restore`, { method: "PATCH", credentials: "include" }),

  bulkAction: (payload: { categories: number[]; action: string }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/bulk`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),
};
