import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/api.types";
import { ProductVariantResponse, ProductVariantRequest, ProductVariantFilterRequest, ProductVariantBulkActionRequest } from "@/types/product-variant.types";

const BASE_ENDPOINT = "/admin/product-variants";

const buildQuery = (filter?: Record<string, unknown>): string => {
  if (!filter) return "";
  const params = new URLSearchParams();
  Object.entries(filter).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") params.append(key, String(value));
  });
  const q = params.toString();
  return q ? `?${q}` : "";
};

export const productVariantService = {
  getAll: (filter?: ProductVariantFilterRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse[]>>(`${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getAllDeleted: (filter?: ProductVariantFilterRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse[]>>(`${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`, { method: "GET", credentials: "include" }),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "GET", credentials: "include" }),

  create: (payload: ProductVariantRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(`${BASE_ENDPOINT}`, { method: "POST", body: JSON.stringify(payload), credentials: "include" }),

  update: (id: number | string, payload: ProductVariantRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(`${BASE_ENDPOINT}/${id}`, { method: "PUT", body: JSON.stringify(payload), credentials: "include" }),

  updateStatus: (id: number | string, payload: { status: string }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),

  softDelete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}`, { method: "DELETE", credentials: "include" }),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/restore`, { method: "PATCH", credentials: "include" }),

  bulkAction: (payload: ProductVariantBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/bulk`, { method: "PATCH", body: JSON.stringify(payload), credentials: "include" }),
};
