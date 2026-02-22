export const CATEGORY_TABLE_HEADERS = [
  'Tên danh mục',
  'Ảnh đại diện',
  'Vị trí',
  'Trạng thái',
  'Tạo bởi',
  'Cập nhật bởi',
  'Hành động'
];

export const STATUS_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "active", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

export const ACTION_OPTIONS = [
  { id: "active", name: "Hoạt động" },
  { id: "inactive", name: "Tạm dừng" },
  { id: "delete", name: "Xóa" },
];

export const CREATOR_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "Lê Văn A", name: "Lê Văn A" },
  { id: "Lê Văn B", name: "Lê Văn B" },
];

export const parentOptions = [
  { value: "", label: "Chọn danh mục" },
  { value: "danh-muc-1", label: "Danh mục 1" },
  { value: "danh-muc-2", label: "Danh mục 2" },
];

export const statusOptions = [
  { value: "active", label: "Hoạt động" },
  { value: "inactive", label: "Tạm dừng" },
];
