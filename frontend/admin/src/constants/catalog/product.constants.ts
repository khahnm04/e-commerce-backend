export const PRODUCT_TABLE_HEADERS = [
  'Tên sản phẩm',
  'Ảnh sản phẩm',
  'Giá hiện tại',
  'Trạng thái',
  'Tạo bởi',
  'Cập nhật bởi',
  'Hành động'
];

export const PRODUCT_STATUS_OPTIONS = [
  { id: "", name: "Trạng thái" },
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
  { id: "DELETE", name: "Xóa" },
];

export const BRAND_OPTIONS = [
  { value: "", label: "Chọn thương hiệu" },
  { value: "1", label: "Apple" },
  { value: "2", label: "Samsung" },
];

export const PRODUCT_STATUS_FORM_OPTIONS = [
  { value: "ACTIVE", label: "Hoạt động" },
  { value: "INACTIVE", label: "Tạm dừng" },
  { value: "OUT_OF_STOCK", label: "Hết hàng" },
  { value: "PENDING", label: "Chờ duyệt" },
  { value: "DISCONTINUED", label: "Ngừng bán" },
];

export const CREATOR_OPTIONS = [
  { id: "", name: "Tất cả" },
  { id: "Lê Văn A", name: "Lê Văn A" },
  { id: "Lê Văn B", name: "Lê Văn B" },
];
