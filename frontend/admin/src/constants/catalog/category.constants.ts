export const CATEGORY_TABLE_HEADERS = [
  "Tên danh mục",
  "Ảnh",
  "Danh mục cha",
  "Vị trí",
  "Trạng thái",
  "Tạo bởi",
  "Cập nhật bởi",
  "Hành động",
];

export const CATEGORY_STATUS_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

export const CATEGORY_ACTION_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "delete", name: "Xóa" },
];

export { TRASH_ACTION_OPTIONS as CATEGORY_TRASH_ACTION_OPTIONS } from "@/constants/common.constants";
