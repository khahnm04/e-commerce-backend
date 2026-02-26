import {
  VariantResponse,
  VariantRequest,
  VariantFilterRequest,
  VariantBulkActionRequest,
} from "@/types/catalog/variant.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/variants";

export const variantService = createCrudService<
  VariantResponse,
  VariantRequest,
  VariantFilterRequest,
  VariantBulkActionRequest
>({
  endpoint: BASE_ENDPOINT,
  hasSlug: false,
});
