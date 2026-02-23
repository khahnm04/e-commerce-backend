"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AuthInput } from "@/components/auth/AuthInput";
import { getLoginSchema, LoginFormValues, LoginMode } from "../login.schemas";
import { authApi } from "@/services/auth.service";

const LOGIN_CONFIG = {
  phone: {
    label: "Số điện thoại",
    placeholder: "Nhập số điện thoại",
    type: "tel" as const,
    toggleText: "Đăng nhập bằng email",
  },
  email: {
    label: "Email",
    placeholder: "Nhập email",
    type: "email" as const,
    toggleText: "Đăng nhập bằng số điện thoại",
  },
};

export default function FormLogin() {
  const router = useRouter();
  const [loginMode, setLoginMode] = useState<LoginMode>("phone");

  const config = LOGIN_CONFIG[loginMode];

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(getLoginSchema(loginMode)),
    mode: "onSubmit",
  });

  const toggleLoginMode = () => {
    const nextMode = loginMode === "phone" ? "email" : "phone";
    setLoginMode(nextMode);
    reset();
  };

  const onSubmit = async (data: LoginFormValues) => {
    try {
      const body =
        loginMode === "phone"
          ? { phoneNumber: data.accountName, password: data.password }
          : { email: data.accountName, password: data.password };

      const result = await authApi.login(body);
      if (!result.success) {
        console.error(result.message);
        return;
      }
      router.refresh();
      router.push("/");
    } catch (error: unknown) {
      console.error("Lỗi:", error instanceof Error ? error.message : error);
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="mb-[24px]"
    >
      <AuthInput
        label={config.label}
        id="accountName"
        type={config.type}
        placeholder={config.placeholder}
        {...register("accountName")}
        error={errors.accountName?.message}
      />

      <AuthInput
        label="Mật khẩu"
        id="password"
        type="password"
        placeholder="Nhập mật khẩu"
        {...register("password")}
        error={errors.password?.message}
      />

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

      <div className="mt-[16px] text-center">
        <button
          type="button"
          onClick={toggleLoginMode}
          className="font-[600] text-[14px] text-[var(--color-primary)] hover:underline cursor-pointer"
        >
          {config.toggleText}
        </button>
      </div>
    </form>
  );
}
