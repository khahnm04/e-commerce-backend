import { z } from "zod";

const phoneRegex = /^(0|\+84)(3[2-9]|5[689]|7[06-9]|8[1-689]|9[0-46-9])[0-9]{7}$/;

export const registerSchema = z.object({
    fullName: z
        .string()
        .min(1, "Vui lòng nhập họ và tên!"),

    dateOfBirth: z
        .string()
        .min(1, "Vui lòng chọn ngày sinh!"),

    phoneNumber: z
        .string()
        .min(1, "Vui lòng nhập số điện thoại!")
        .regex(phoneRegex, "Số điện thoại không đúng định dạng!"),

    email: z
        .string()
        .min(1, "Vui lòng nhập email!")
        .email("Email không đúng định dạng!"),

    password: z
        .string()
        .min(1, "Vui lòng nhập mật khẩu của bạn!")
        .min(8, "Mật khẩu phải chứa ít nhất 8 ký tự!")
        .regex(/[A-Z]/, "Mật khẩu phải chứa ký tự viết hoa!")
        .regex(/[a-z]/, "Mật khẩu phải chứa ký tự viết thường!")
        .regex(/[0-9]/, "Mật khẩu phải chứa chữ số!")
        .regex(/[^a-zA-Z0-9]/, "Mật khẩu phải chứa ký tự đặc biệt!"),

    confirmPassword: z
        .string()
        .min(1, "Vui lòng nhập lại mật khẩu!"),
}).refine((data) => data.password === data.confirmPassword, {
    message: "Mật khẩu nhập lại không khớp!",
    path: ["confirmPassword"],
});

export type RegisterFormValues = z.infer<typeof registerSchema>;
