import {
  CategoryResponse,
  CategoryRequest,
  CategoryFilterRequest,
  CategoryBulkActionRequest,
} from "@/types/catalog/category.types";
import { createCrudService } from "../common/crud.service";

export const categoryService = createCrudService<
  CategoryResponse,
  CategoryRequest,
  CategoryFilterRequest,
  CategoryBulkActionRequest
>({
  endpoint: "/admin/categories",
  hasSlug: true,
});
