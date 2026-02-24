export interface PageMetadata {
  pageNo: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  isFirst: boolean;
  isLast: boolean;
  isEmpty: boolean;
}

export interface ApiResponse<T> {
  timestamp: string;
  success: boolean;
  message: string;
  meta?: PageMetadata;
  data: T;
}

export interface FieldError {
  field: string;
  message: string;
}

export interface ErrorResponse {
  timestamp: string;
  success: boolean;
  code: number;
  message: string;
  path: string;
  method: string;
  errors?: FieldError[];
}

export interface PaginatedData<T> {
  content: T[];
  meta: PageMetadata;
}
