import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import {
  VariantValueResponse,
  VariantValueRequest,
  VariantValueFilterRequest,
  VariantValueBulkActionRequest,
} from "@/types/catalog/variant-value.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/variant-values";

export const variantValueService = {
  ...createCrudService<
    VariantValueResponse,
    VariantValueRequest,
    VariantValueFilterRequest,
    VariantValueBulkActionRequest
  >({
    endpoint: BASE_ENDPOINT,
    hasSlug: false,
  }),
  updateStatus: (id: number | string, payload: { status: string }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
};
