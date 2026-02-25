export const EMPLOYEE_TABLE_HEADERS = [
    'Mã NV',
    'Họ tên',
    'Ảnh đại diện',
    'Số điện thoại',
    'Email',
    'Chức vụ',
    'Chi nhánh',
    'Trạng thái',
    'Tạo bởi',
    'Cập nhật bởi',
    'Hành động'
];

export const EMPLOYEE_STATUS_OPTIONS = [
    { id: "", name: "Tất cả" },
    { id: "ACTIVE", name: "Hoạt động" },
    { id: "INACTIVE", name: "Tạm dừng" },
    { id: "BANNED", name: "Bị cấm" },
    { id: "LOCKED", name: "Bị khóa" },
];

export const EMPLOYEE_ACTION_OPTIONS = [
    { id: "ACTIVE", name: "Hoạt động" },
    { id: "INACTIVE", name: "Tạm dừng" },
    { id: "BANNED", name: "Bị cấm" },
    { id: "LOCKED", name: "Bị khóa" },
    { id: "delete", name: "Xóa" },
];

export { TRASH_ACTION_OPTIONS as EMPLOYEE_TRASH_ACTION_OPTIONS } from "@/constants/common.constants";

export const EMPLOYEE_GENDER_OPTIONS = [
    { id: "", name: "Tất cả" },
    { id: "MALE", name: "Nam" },
    { id: "FEMALE", name: "Nữ" },
    { id: "OTHER", name: "Khác" },
];
