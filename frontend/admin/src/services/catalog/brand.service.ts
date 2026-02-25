import {
  BrandResponse,
  BrandRequest,
  BrandFilterRequest,
  BrandBulkActionRequest,
} from "@/types/catalog/brand.types";
import { createCrudService } from "../common/crud.service";

export const brandService = createCrudService<
  BrandResponse,
  BrandRequest,
  BrandFilterRequest,
  BrandBulkActionRequest
>({
  endpoint: "/admin/brands",
  hasSlug: true,
});
