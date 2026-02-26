"use client";
import { useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-hot-toast";
import { AuthInput } from "@/components/auth/AuthInput";
import { getLoginSchema, LoginFormValues, LoginMode } from "../login.schemas";
import { authApi } from "@/services/auth/auth.service";

const REDIRECT_ON_LOGIN = "/";

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

const buildLoginPayload = (data: LoginFormValues, mode: LoginMode) => {
  const basePayload = { password: data.password };
  return mode === "phone"
    ? { ...basePayload, phoneNumber: data.accountName }
    : { ...basePayload, email: data.accountName };
};

export default function FormLogin() {
  const router = useRouter();
  const [loginMode, setLoginMode] = useState<LoginMode>("phone");

  const config = LOGIN_CONFIG[loginMode];
  const schema = useMemo(() => getLoginSchema(loginMode), [loginMode]);

  const {
    register,
    handleSubmit,
    resetField,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(schema),
    mode: "onSubmit",
  });

  const toggleLoginMode = () => {
    if (isSubmitting) return;
    const nextMode = loginMode === "phone" ? "email" : "phone";
    setLoginMode(nextMode);
    resetField("accountName");
  };

  const onSubmit = async (data: LoginFormValues) => {
    try {
      const body = buildLoginPayload(data, loginMode);

      const result = await authApi.login(body);
      if (!result.success) {
        toast.error(result.message || "Đăng nhập thất bại!");
        return;
      }

      router.refresh();
      router.push(REDIRECT_ON_LOGIN);
    } catch (error: unknown) {
      const message = error instanceof Error ? error.message : "Có lỗi xảy ra, vui lòng thử lại!";
      toast.error(message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="mb-[24px]">
      <AuthInput
        label={config.label}
        id="accountName"
        type={config.type}
        placeholder={config.placeholder}
        autoComplete="username"
        {...register("accountName")}
        error={errors.accountName?.message}
      />

      <AuthInput
        label="Mật khẩu"
        id="password"
        type="password"
        placeholder="Nhập mật khẩu"
        autoComplete="current-password"
        {...register("password")}
        error={errors.password?.message}
      />

      <div className="mb-[24px] flex items-center gap-[9.6px]">
        <input
          type="checkbox"
          id="remember-password"
          {...register("rememberPassword")}
          className="w-[19.2px] h-[19.2px]"
        />
        <label
          htmlFor="remember-password"
          className="font-[600] text-[14.4px] text-[var(--color-text)] opacity-60 cursor-pointer"
        >
          Nhớ mật khẩu
        </label>
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="w-full h-[44.8px] rounded-[8px] bg-[var(--color-primary)] font-[700] text-[16px] text-white disabled:opacity-60 flex items-center justify-center disabled:cursor-not-allowed"
      >
        {isSubmitting ? (
          <>
            <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"></div>
            Đang đăng nhập...
          </>
        ) : (
          "Đăng nhập"
        )}
      </button>

      <div className="mt-[16px] text-center">
        <button
          type="button"
          onClick={toggleLoginMode}
          disabled={isSubmitting}
          className="font-[600] text-[14px] text-[var(--color-primary)] hover:underline cursor-pointer disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:no-underline"
        >
          {config.toggleText}
        </button>
      </div>
    </form>
  );
}
