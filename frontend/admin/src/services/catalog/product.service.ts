import {
  ProductResponse,
  ProductRequest,
  ProductFilterRequest,
  ProductBulkActionRequest,
} from "@/types/catalog/product.types";
import { createCrudService } from "../common/crud.service";

export const productService = createCrudService<
  ProductResponse,
  ProductRequest,
  ProductFilterRequest,
  ProductBulkActionRequest
>({
  endpoint: "/admin/products",
  hasSlug: true,
});
