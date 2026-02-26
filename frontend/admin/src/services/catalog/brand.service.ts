import {
  BrandResponse,
  BrandRequest,
  BrandFilterRequest,
  BrandBulkActionRequest,
} from "@/types/catalog/brand.types";
import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/brands";

export const brandService = {
  ...createCrudService<BrandResponse, BrandRequest, BrandFilterRequest, BrandBulkActionRequest>({
    endpoint: BASE_ENDPOINT,
    hasSlug: true,
  }),
  updateStatus: (id: number | string, payload: { status: string | number }) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
};
