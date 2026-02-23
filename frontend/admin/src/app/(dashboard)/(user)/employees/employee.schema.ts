import { z } from "zod";

const EmployeeBase = z.object({
  fullName: z.string().min(1, "Họ tên không được để trống"),
  email: z.string().email("Email không hợp lệ"),
  phoneNumber: z.string().min(1, "Số điện thoại không được để trống"),
  code: z.string().min(1, "Mã nhân viên không được để trống"),
  branchId: z.number().min(1, "Vui lòng chọn chi nhánh"),
  jobTitle: z.string().optional(),
  dateOfBirth: z.string().optional().nullable(),
  avatarUrl: z.string().optional().default(""),
  gender: z.enum(["MALE", "FEMALE", "OTHER"]).optional(),
  status: z.enum(["ACTIVE", "INACTIVE", "BANNED", "LOCKED"]).default("ACTIVE"),
  roles: z.array(z.number()).default([]),
});

export const CreateEmployeeSchema = EmployeeBase.extend({
  password: z.string().min(6, "Mật khẩu phải ít nhất 6 ký tự").optional(),
  confirmPassword: z.string().optional(),
});

export const UpdateEmployeeSchema = EmployeeBase;

export type CreateEmployeeValues = z.infer<typeof CreateEmployeeSchema>;
export type UpdateEmployeeValues = z.infer<typeof UpdateEmployeeSchema>;
