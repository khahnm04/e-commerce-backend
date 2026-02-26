import { z } from "zod";

export type LoginMode = "phone" | "email";

const phoneRegex = /^(0|\+84)(3[2-9]|5[689]|7[06-9]|8[1-689]|9[0-46-9])[0-9]{7}$/;

const passwordSchema = z
  .string()
  .min(8, "Mật khẩu phải có ít nhất 8 ký tự")
  .regex(/[A-Z]/, "Phải chứa chữ hoa")
  .regex(/[a-z]/, "Phải chứa chữ thường")
  .regex(/[0-9]/, "Phải chứa chữ số")
  .regex(/[^a-zA-Z0-9]/, "Phải chứa ký tự đặc biệt");

export const phoneLoginSchema = z.object({
  accountName: z
    .string()
    .min(1, "Vui lòng nhập số điện thoại")
    .regex(phoneRegex, "Số điện thoại không đúng định dạng"),
  password: passwordSchema,
});

export const emailLoginSchema = z.object({
  accountName: z.string().min(1, "Vui lòng nhập email").email("Email không đúng định dạng"),
  password: passwordSchema,
});

export const getLoginSchema = (mode: LoginMode) =>
  mode === "phone" ? phoneLoginSchema : emailLoginSchema;

export type LoginFormValues = z.infer<typeof phoneLoginSchema>;
