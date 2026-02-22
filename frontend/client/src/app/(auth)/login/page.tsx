/* eslint-disable @next/next/no-img-element */
"use client"
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { InputField } from "@/components/auth/InputField";
import { AppButton } from "@/components/auth/AppButton";
import { SocialButton } from "@/components/auth/SocialButton";
import { loginSchema, LoginFormValues } from "./login.schemas";

type LoginField = {
  label: string;
  id: keyof LoginFormValues;
  type: React.HTMLInputTypeAttribute;
  placeholder: string;
};

export default function LoginPage() {
  const router = useRouter();

  const inputFields: LoginField[] = [
    {
      id: "phoneNumber",
      label: "Số điện thoại",
      type: "text",
      placeholder: "Nhập số điện thoại của bạn"
    },
    {
      id: "password",
      label: "Mật khẩu",
      type: "password",
      placeholder: "Nhập mật khẩu của bạn"
    }
  ];

  const socialButtons = [
    {
      icon: "/assets/images/icon-google.jpg",
      label: "Google"
    },
    {
      icon: "/assets/images/icon-facebook.jpg",
      label: "Facebook"
    }
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
      const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          phoneNumber: data.phoneNumber,
          password: data.password
        }),
        credentials: "include"
      });

      const result = await res.json();
      console.log("Login response data:", result);

      if (!result.success) {
        alert(result.message || "Đăng nhập thất bại");
        return;
      }

      console.log("Login successful!");
      // Lưu thông tin user vào localStorage nếu có
      if (result.data?.user?.fullName) {
        console.log("Saving fullName to localStorage:", result.data.user.fullName);
        localStorage.setItem('userFullName', result.data.user.fullName);
        window.dispatchEvent(new Event('userUpdated'));
      }
      router.push("/");
    } catch (error) {
      console.error("Error:", error);
      alert("Có lỗi xảy ra khi đăng nhập");
    }
  };

  return (
    <>
      <div className="min-h-screen bg-white flex items-center justify-center py-[60px]">
        <div className="w-full max-w-[436px] px-[16px]">

          <h2 className="font-[700] text-[33px] text-[#D70018] text-center mb-[40px]">
            Đăng nhập
          </h2>

          <form
            onSubmit={handleSubmit(onSubmit)}
            className="flex flex-col gap-[24px] mb-8 w-full"
          >
            {inputFields.map((field) => (
              <InputField
                key={field.id}
                label={field.label}
                type={field.type}
                id={field.id}
                placeholder={field.placeholder}
                {...register(field.id)}
                error={errors[field.id]?.message}
              />
            ))}

            <div className="mt-2">
              <AppButton disabled={isSubmitting}>
                {isSubmitting ? "Đang đăng nhập..." : "Đăng nhập"}
              </AppButton>
              <div className="flex justify-center px-[12px] py-[16px] hover:underline decoration-[#3B82F6]">
                <Link
                  className="font-medium text-base text-[#3B82F6]"
                  href={""}
                >
                  Quên mật khẩu?
                </Link>
              </div>
            </div>
          </form>

          <div className="mb-[70px] w-full">
            <div className="flex items-center gap-4 mb-6">
              <div className="flex-1 h-px bg-[#E4E4E7]"></div>
              <span className="text-sm text-[#71717A] whitespace-nowrap">
                Hoặc đăng nhập bằng
              </span>
              <div className="flex-1 h-px bg-[#E4E4E7]"></div>
            </div>

            <div className="flex md:gap-[20px] gap-[10px] w-full items-center justify-center">
              {socialButtons.map((social) => (
                <SocialButton
                  key={social.label}
                  icon={social.icon}
                  label={social.label}
                />
              ))}
            </div>
          </div>

          <div className="flex justify-center items-center gap-2 text-base">
            <span className="text-[#71717A]">
              Bạn chưa có tài khoản?
            </span>
            <div className="hover:underline decoration-[#D70018]">
              <Link
                className="text-[#D70018] font-medium hover:underline cursor-pointer"
                href={"/register"}
              >
                Đăng ký ngay
              </Link>
            </div>
          </div>

        </div>
      </div>
    </>
  )
}