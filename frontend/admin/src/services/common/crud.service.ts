import { sendRequest } from "@/lib/api-client";
import { buildQuery } from "@/lib/query-builder";
import { ApiResponse } from "@/types/common/api.types";

interface CrudConfig {
  endpoint: string;
  hasSlug?: boolean;
}

export function createCrudService<
  TResponse,
  TRequest,
  TFilter = Record<string, unknown>,
  TBulkAction = { ids: number[]; action: string },
  TCreateRequest = TRequest,
  TUpdateRequest = TRequest,
  TUpdateStatusRequest = { status: any }
>(config: CrudConfig) {
  const { endpoint, hasSlug = false } = config;

  return {
    getAll: (filter?: TFilter) =>
      sendRequest<ApiResponse<TResponse[]>>(
        `${endpoint}${buildQuery(filter as Record<string, unknown>)}`,
        { method: "GET" }
      ),

    getAllDeleted: (filter?: TFilter) =>
      sendRequest<ApiResponse<TResponse[]>>(
        `${endpoint}/deleted${buildQuery(filter as Record<string, unknown>)}`,
        { method: "GET" }
      ),

    getById: (idOrSlug: string | number) =>
      sendRequest<ApiResponse<TResponse>>(
        hasSlug ? `${endpoint}/slug/${idOrSlug}` : `${endpoint}/${idOrSlug}`,
        { method: "GET" }
      ),

    getDeletedById: (id: number | string) =>
      sendRequest<ApiResponse<TResponse>>(
        `${endpoint}/deleted/${id}`,
        { method: "GET" }
      ),

    create: (payload: TCreateRequest) =>
      sendRequest<ApiResponse<TResponse>>(
        `${endpoint}`,
        { method: "POST", body: JSON.stringify(payload) }
      ),

    update: (id: number | string, payload: TUpdateRequest) =>
      sendRequest<ApiResponse<TResponse>>(
        `${endpoint}/${id}`,
        { method: "PUT", body: JSON.stringify(payload) }
      ),

    updateStatus: (id: number | string, payload: TUpdateStatusRequest) =>
      sendRequest<ApiResponse<void>>(
        `${endpoint}/${id}/status`,
        { method: "PATCH", body: JSON.stringify(payload) }
      ),

    softDelete: (id: number | string) =>
      sendRequest<ApiResponse<void>>(
        `${endpoint}/${id}`,
        { method: "DELETE" }
      ),

    restore: (id: number | string) =>
      sendRequest<ApiResponse<void>>(
        `${endpoint}/${id}/restore`,
        { method: "PATCH" }
      ),

    bulkAction: (payload: TBulkAction) =>
      sendRequest<ApiResponse<void>>(
        `${endpoint}/bulk`,
        { method: "PATCH", body: JSON.stringify(payload) }
      ),
  };
}
