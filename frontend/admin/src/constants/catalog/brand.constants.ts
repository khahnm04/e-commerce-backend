export const BRAND_TABLE_HEADERS = [
  "Tên nhãn hàng",
  "Logo",
  "Slug",
  "Quốc gia",
  "Trạng thái",
  "Tạo bởi",
  "Cập nhật bởi",
  "Hành động",
];

export const BRAND_STATUS_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
];

export const BRAND_ACTION_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "delete", name: "Xóa" },
];

export { TRASH_ACTION_OPTIONS as BRAND_TRASH_ACTION_OPTIONS } from "@/constants/common.constants";
