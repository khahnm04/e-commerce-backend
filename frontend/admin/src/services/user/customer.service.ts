import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import {
  CustomerResponse,
  CustomerCreationRequest,
  CustomerUpdateRequest,
  CustomerUpdateStatusRequest,
  CustomerFilterRequest,
  CustomerBulkActionRequest,
} from "@/types/user/customer.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/customers";

export const customerService = {
  ...createCrudService<
    CustomerResponse,
    CustomerCreationRequest,
    CustomerFilterRequest,
    CustomerBulkActionRequest,
    CustomerCreationRequest,
    CustomerUpdateRequest
  >({
    endpoint: BASE_ENDPOINT,
    hasSlug: false,
  }),
  updateStatus: (id: number | string, payload: CustomerUpdateStatusRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
  changePassword: (
    id: number | string,
    payload: { currentPassword: string; newPassword: string; confirmNewPassword: string },
  ) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/change-password`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
};
