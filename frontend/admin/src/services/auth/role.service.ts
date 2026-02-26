import {
  RoleResponse,
  RoleRequest,
  RoleFilterRequest,
  RoleBulkActionRequest,
} from "@/types/auth/role.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/roles";

const crud = createCrudService<
  RoleResponse,
  RoleRequest,
  RoleFilterRequest,
  RoleBulkActionRequest
>({
  endpoint: BASE_ENDPOINT,
  hasSlug: false,
});

export const roleService = {
  ...crud,
  delete: crud.softDelete,
};
