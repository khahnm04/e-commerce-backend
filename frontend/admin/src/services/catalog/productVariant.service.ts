import {
  ProductVariantResponse,
  ProductVariantRequest,
  ProductVariantFilterRequest,
  ProductVariantBulkActionRequest,
} from "@/types/catalog/product-variant.types";
import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/product-variants";

export const productVariantService = {
  ...createCrudService<
    ProductVariantResponse,
    ProductVariantRequest,
    ProductVariantFilterRequest,
    ProductVariantBulkActionRequest
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
