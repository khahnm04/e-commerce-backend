export interface BaseEntity {
  id: number;

  createdAt: string;
  createdBy: number;
  createdName: string;

  updatedAt: string;
  updatedBy: number;
  updatedName: string;

  deletedAt?: string | null;
}

export interface BaseFilterRequest {
  page?: number;
  size?: number;
  sort?: string;
  keyword?: string;
  dateType?: string;
  startDate?: string;
  endDate?: string;
}

export interface BaseBulkActionRequest {
  action: string;
}
