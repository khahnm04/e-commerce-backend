import {
  AttributeResponse,
  AttributeRequest,
  AttributeFilterRequest,
  AttributeBulkActionRequest,
} from "@/types/catalog/attribute.types";
import { createCrudService } from "../common/crud.service";

export const attributeService = createCrudService<
  AttributeResponse,
  AttributeRequest,
  AttributeFilterRequest,
  AttributeBulkActionRequest
>({
  endpoint: "/admin/attributes",
  hasSlug: false,
});
