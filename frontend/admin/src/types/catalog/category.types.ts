import { CategoryStatus } from "@/constants/enums";
import { BaseEntity } from "../common/base.types";

export interface CategoryPayload {
  name: string;
  slug?: string | null;
  description?: string;
  status: CategoryStatus | string;
  parentId?: number | null;
  position?: number;
}

export interface Category extends BaseEntity {
  name: string;
  slug: string;
  description?: string;
  image?: string;
  parentId?: number | null;
  position?: number;
  path?: string;
  status: CategoryStatus;
}
