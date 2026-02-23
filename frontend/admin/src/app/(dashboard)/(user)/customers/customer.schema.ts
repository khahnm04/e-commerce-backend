import { z } from "zod";

const CustomerBase = z.object({
  fullName: z.string().min(1, "Họ tên không được để trống"),
  email: z.string().email("Email không hợp lệ"),
  phoneNumber: z.string().min(1, "Số điện thoại không được để trống"),
  dateOfBirth: z.string().optional().nullable(),
  avatarUrl: z.string().optional().default(""),
  gender: z.enum(["MALE", "FEMALE", "OTHER"]).optional(),
  status: z.enum(["ACTIVE", "INACTIVE", "BANNED", "LOCKED"]).default("ACTIVE"),
});

export const CreateCustomerSchema = CustomerBase.extend({
  password: z.string().min(6, "Mật khẩu phải ít nhất 6 ký tự").optional(),
  confirmPassword: z.string().optional(),
});

export const UpdateCustomerSchema = CustomerBase;

export type CreateCustomerValues = z.infer<typeof CreateCustomerSchema>;
export type UpdateCustomerValues = z.infer<typeof UpdateCustomerSchema>;
