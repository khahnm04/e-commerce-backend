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
