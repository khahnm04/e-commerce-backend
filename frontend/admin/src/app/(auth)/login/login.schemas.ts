import { z } from "zod";

export const loginSchema = z.object({
  accountName: z
    .string()
    .min(1, "Vui lòng nhập tài khoản"),

  password: z
    .string()
    .min(8, "Mật khẩu phải có ít nhất 8 ký tự")
    .regex(/[A-Z]/, "Phải chứa chữ hoa")
    .regex(/[a-z]/, "Phải chứa chữ thường")
    .regex(/[0-9]/, "Phải chứa chữ số")
    .regex(/[^a-zA-Z0-9]/, "Phải chứa ký tự đặc biệt"),
});

export type LoginFormValues = z.infer<typeof loginSchema>;
