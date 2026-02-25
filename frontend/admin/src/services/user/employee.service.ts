import {
  EmployeeResponse,
  EmployeeCreationRequest,
  EmployeeUpdateRequest,
  EmployeeUpdateStatusRequest,
  EmployeeFilterRequest,
  EmployeeBulkActionRequest,
} from "@/types/user/employee.types";
import { createCrudService } from "../common/crud.service";

export const employeeService = createCrudService<
  EmployeeResponse,
  EmployeeCreationRequest,
  EmployeeFilterRequest,
  EmployeeBulkActionRequest,
  EmployeeCreationRequest,
  EmployeeUpdateRequest,
  EmployeeUpdateStatusRequest
>({
  endpoint: "/admin/employees",
  hasSlug: false,
});
