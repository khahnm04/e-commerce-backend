import { sendRequest } from "@/lib/api-client";
import { buildQuery } from "@/lib/query-builder";
import { ApiResponse } from "@/types/common/api.types";
import {
  VariantValueResponse,
  VariantValueRequest,
  VariantValueFilterRequest,
  VariantValueBulkActionRequest,
} from "@/types/catalog/variant-value.types";

const BASE_ENDPOINT = "/admin/variant-values";

export const variantValueService = {
  getAll: (filter?: VariantValueFilterRequest) =>
    sendRequest<ApiResponse<VariantValueResponse[]>>(
      `${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getAllDeleted: (filter?: VariantValueFilterRequest) =>
    sendRequest<ApiResponse<VariantValueResponse[]>>(
      `${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<VariantValueResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "GET" }
    ),

  create: (payload: VariantValueRequest) =>
    sendRequest<ApiResponse<VariantValueResponse>>(
      `${BASE_ENDPOINT}`,
      { method: "POST", body: JSON.stringify(payload) }
    ),

  update: (id: number | string, payload: VariantValueRequest) =>
    sendRequest<ApiResponse<VariantValueResponse>>(
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

  bulkAction: (payload: VariantValueBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/bulk`,
      { method: "PATCH", body: JSON.stringify(payload) }
    ),
};
