import { sendRequest } from "@/lib/api-client";
import { buildQuery } from "@/lib/query-builder";
import { ApiResponse } from "@/types/common/api.types";
import {
  ProductVariantResponse,
  ProductVariantRequest,
  ProductVariantFilterRequest,
  ProductVariantBulkActionRequest,
} from "@/types/catalog/product-variant.types";

const BASE_ENDPOINT = "/admin/product-variants";

export const productVariantService = {
  getAll: (filter?: ProductVariantFilterRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse[]>>(
      `${BASE_ENDPOINT}${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getAllDeleted: (filter?: ProductVariantFilterRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse[]>>(
      `${BASE_ENDPOINT}/deleted${buildQuery(filter as Record<string, unknown>)}`,
      { method: "GET" }
    ),

  getById: (id: number | string) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(
      `${BASE_ENDPOINT}/${id}`,
      { method: "GET" }
    ),

  create: (payload: ProductVariantRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(
      `${BASE_ENDPOINT}`,
      { method: "POST", body: JSON.stringify(payload) }
    ),

  update: (id: number | string, payload: ProductVariantRequest) =>
    sendRequest<ApiResponse<ProductVariantResponse>>(
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

  bulkAction: (payload: ProductVariantBulkActionRequest) =>
    sendRequest<ApiResponse<void>>(
      `${BASE_ENDPOINT}/bulk`,
      { method: "PATCH", body: JSON.stringify(payload) }
    ),
};
