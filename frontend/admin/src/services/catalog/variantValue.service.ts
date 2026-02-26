import {
  VariantValueResponse,
  VariantValueRequest,
  VariantValueFilterRequest,
  VariantValueBulkActionRequest,
} from "@/types/catalog/variant-value.types";
import { createCrudService } from "../common/crud.service";

const BASE_ENDPOINT = "/admin/variant-values";

export const variantValueService = createCrudService<
  VariantValueResponse,
  VariantValueRequest,
  VariantValueFilterRequest,
  VariantValueBulkActionRequest
>({
  endpoint: BASE_ENDPOINT,
  hasSlug: false,
});
