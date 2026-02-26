import {
  EmployeeResponse,
  EmployeeCreationRequest,
  EmployeeUpdateRequest,
  EmployeeUpdateStatusRequest,
  EmployeeFilterRequest,
  EmployeeBulkActionRequest,
} from "@/types/user/employee.types";
import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/employees";

export const employeeService = {
  ...createCrudService<
    EmployeeResponse,
    EmployeeCreationRequest,
    EmployeeFilterRequest,
    EmployeeBulkActionRequest,
    EmployeeCreationRequest,
    EmployeeUpdateRequest
  >({
    endpoint: BASE_ENDPOINT,
    hasSlug: false,
  }),
  updateStatus: (id: number | string, payload: EmployeeUpdateStatusRequest) =>
    sendRequest<ApiResponse<void>>(`${BASE_ENDPOINT}/${id}/status`, {
      method: "PATCH",
      body: JSON.stringify(payload),
    }),
};
