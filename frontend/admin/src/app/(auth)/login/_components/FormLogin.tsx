"use client";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AuthInput } from "@/components/auth/AuthInput";
import { loginSchema, LoginFormValues } from "../login.schemas";
import { authApi } from "@/services/auth.service";

type LoginField = {
  label: string;
  id: keyof LoginFormValues;
  type: React.HTMLInputTypeAttribute;
};

export default function FormLogin() {
  const router = useRouter();

  const formFields: LoginField[] = [
    {
      label: "Tài khoản",
      id: "accountName",
      type: "text",
    },
    {
      label: "Mật khẩu",
      id: "password",
      type: "password",
    },
  ];

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    mode: "onSubmit",
  });

  const onSubmit = async (data: LoginFormValues) => {
    try {
      const result = await authApi.login(data);
      if (!result.success) {
        console.error(result.message);
        return;
      }
      router.refresh();
      router.push("/");
    } catch (error: any) {
      console.error("Lỗi:", error.message);
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="mb-[24px]"
    >
      {formFields && (
        formFields.map(field => (
          <AuthInput
            key={field.id}
            label={field.label}
            id={field.id}
            type={field.type}
            {...register(field.id)}
            error={errors[field.id]?.message}
          />
        ))
      )}

      <div className="mb-[24px] flex items-center gap-[9.6px]">
        <input
          type="checkbox"
          id="remember-password"
          name="rememberPassword"
          className="w-[19.2px] h-[19.2px]"
        />
        <label
          htmlFor="remember-password"
          className="font-[600] text-[14.4px] text-[var(--color-text)] opacity-60"
        >
          Nhớ mật khẩu
        </label>
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="w-full h-[44.8px] rounded-[8px] bg-[var(--color-primary)] font-[700] text-[16px] text-white disabled:opacity-60"
      >
        {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
      </button>
    </form>
  );
}
