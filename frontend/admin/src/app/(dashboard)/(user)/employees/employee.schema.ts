import { z } from "zod";

const EmployeeBase = z.object({
  fullName: z.string().min(1, "Họ tên không được để trống"),
  email: z.string().regex(/^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/, "Email không hợp lệ"),
  phoneNumber: z.string().regex(/^(?:\+84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/, "Số điện thoại không hợp lệ (phải là số VN)"),
  code: z.string().min(1, "Mã nhân viên không được để trống"),
  branchId: z.number().min(1, "Vui lòng chọn chi nhánh"),
  jobTitle: z.string().optional(),
  dateOfBirth: z.string().optional().nullable(),
  avatarUrl: z.string().optional().default(""),
  gender: z.enum(["MALE", "FEMALE", "OTHER"]).optional(),
  status: z.enum(["ACTIVE", "INACTIVE", "BANNED", "LOCKED"]).default("ACTIVE"),
  roles: z.array(z.number()).min(1, "Vui lòng chọn ít nhất 1 nhóm quyền"),
});

export const CreateEmployeeSchema = EmployeeBase.extend({
  password: z.string().min(6, "Mật khẩu phải ít nhất 6 ký tự"),
  confirmPassword: z.string().min(1, "Vui lòng xác nhận mật khẩu"),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Mật khẩu xác nhận không khớp",
  path: ["confirmPassword"],
});

export const UpdateEmployeeSchema = EmployeeBase.extend({
  password: z.string().optional(),
  confirmPassword: z.string().optional(),
});

export type CreateEmployeeValues = z.infer<typeof CreateEmployeeSchema>;
export type UpdateEmployeeValues = z.infer<typeof UpdateEmployeeSchema>;
