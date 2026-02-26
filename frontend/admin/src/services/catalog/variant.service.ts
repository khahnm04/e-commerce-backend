import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import {
  VariantResponse,
  VariantRequest,
  VariantFilterRequest,
  VariantBulkActionRequest,
} from "@/types/catalog/variant.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/variants";

export const variantService = {
  ...createCrudService<
    VariantResponse,
    VariantRequest,
    VariantFilterRequest,
    VariantBulkActionRequest
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
