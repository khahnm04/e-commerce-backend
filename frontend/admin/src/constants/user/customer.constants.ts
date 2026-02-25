export const CUSTOMER_TABLE_HEADERS = [
    'Họ tên',
    'Ảnh đại diện',
    'Số điện thoại',
    'Email',
    'Giới tính',
    'Trạng thái',
    'Tạo bởi',
    'Cập nhật bởi',
    'Hành động'
];

export const CUSTOMER_STATUS_OPTIONS = [
    { id: "", name: "Tất cả" },
    { id: "ACTIVE", name: "Hoạt động" },
    { id: "INACTIVE", name: "Tạm dừng" },
    { id: "BANNED", name: "Bị cấm" },
    { id: "LOCKED", name: "Bị khóa" },
];

export const CUSTOMER_ACTION_OPTIONS = [
    { id: "ACTIVE", name: "Hoạt động" },
    { id: "INACTIVE", name: "Tạm dừng" },
    { id: "BANNED", name: "Bị cấm" },
    { id: "LOCKED", name: "Bị khóa" },
    { id: "delete", name: "Xóa" },
];

export const CUSTOMER_TRASH_ACTION_OPTIONS = [
    { id: "restore", name: "Khôi phục" },
];

export const GENDER_OPTIONS = [
    { id: "", name: "Tất cả" },
    { id: "MALE", name: "Nam" },
    { id: "FEMALE", name: "Nữ" },
    { id: "OTHER", name: "Khác" },
];
