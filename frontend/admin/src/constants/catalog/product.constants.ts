export const PRODUCT_TABLE_HEADERS = [
  "Tên sản phẩm",
  "Ảnh",
  "Mã SP",
  "Giá bán",
  "Nhãn hàng",
  "Trạng thái",
  "Tạo bởi",
  "Cập nhật bởi",
  "Hành động",
];

export const PRODUCT_STATUS_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "OUT_OF_STOCK", name: "Hết hàng" },
  { id: "PENDING", name: "Chờ duyệt" },
  { id: "DISCONTINUED", name: "Ngừng bán" },
];

export const PRODUCT_ACTION_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "OUT_OF_STOCK", name: "Hết hàng" },
  { id: "delete", name: "Xóa" },
];

export { TRASH_ACTION_OPTIONS as PRODUCT_TRASH_ACTION_OPTIONS } from "@/constants/common.constants";
