import {
  ProductResponse,
  ProductRequest,
  ProductFilterRequest,
  ProductBulkActionRequest,
} from "@/types/catalog/product.types";
import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/products";

export const productService = {
  ...createCrudService<
    ProductResponse,
    ProductRequest,
    ProductFilterRequest,
    ProductBulkActionRequest
  >({
    endpoint: BASE_ENDPOINT,
    hasSlug: true,
  }),
  updateStatus: (id: number | string, payload: { status: string | number }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
};
