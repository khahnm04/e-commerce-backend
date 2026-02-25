import { sendRequest } from "@/lib/api-client";
import { buildQuery } from "@/lib/query-builder";
import { ApiResponse } from "@/types/common/api.types";
import {
  VariantResponse,
  VariantRequest,
  VariantFilterRequest,
  VariantBulkActionRequest,
} from "@/types/catalog/variant.types";

const BASE_ENDPOINT = "/admin/variants";

export const variantService = {
  getAll: (filter?: VariantFilterRequest) =>
    sendRequest<ApiResponse<VariantResponse[]>>(
      `${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getAllDeleted: (filter?: VariantFilterRequest) =>
    sendRequest<ApiResponse<VariantResponse[]>>(
      `${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<VariantResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "GET" }
    ),

  create: (payload: VariantRequest) =>
    sendRequest<ApiResponse<VariantResponse>>(
      `${BASE_ENDPOINT}`,
      { method: "POST", body: JSON.stringify(payload) }
    ),

  update: (id: number | string, payload: VariantRequest) =>
    sendRequest<ApiResponse<VariantResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "PUT", body: JSON.stringify(payload) }
    ),

  updateStatus: (id: number | string, payload: { status: string }) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/${id}/status`,
      { method: "PATCH", body: JSON.stringify(payload) }
    ),

  softDelete: (id: number | string) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "DELETE" }
    ),

  restore: (id: number | string) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/${id}/restore`,
      { method: "PATCH" }
    ),

  bulkAction: (payload: VariantBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/bulk`,
      { method: "PATCH", body: JSON.stringify(payload) }
    ),
};
