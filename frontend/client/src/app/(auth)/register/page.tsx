"use client";
import { FaChevronLeft } from "react-icons/fa";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { SocialButton } from "@/components/auth/SocialButton";
import { InputField } from "@/components/auth/InputField";
import { registerSchema, RegisterFormValues } from "./register.schemas";

type RegisterField = {
  id: keyof RegisterFormValues;
  label: string;
  type: React.HTMLInputTypeAttribute;
  placeholder: string;
};

export default function RegisterPage() {
  const router = useRouter();

  const inputFieldInfos: RegisterField[] = [
    {
      id: "fullName",
      label: "Họ và tên",
      type: "text",
      placeholder: "Nhập họ tên"
    },
    {
      id: "dateOfBirth",
      label: "Ngày sinh",
      type: "date",
      placeholder: ""
    },
    {
      id: "phoneNumber",
      label: "Số điện thoại",
      type: "tel",
      placeholder: "Nhập số điện thoại"
    },
    {
      id: "email",
      label: "Email",
      type: "email",
      placeholder: "Nhập email"
    }
  ];

  const inputFieldPasswords: RegisterField[] = [
    {
      id: "password",
      label: "Mật khẩu",
      type: "password",
      placeholder: "Nhập mật khẩu của bạn"
    },
    {
      id: "confirmPassword",
      label: "Nhập lại mật khẩu",
      type: "password",
      placeholder: "Nhập lại mật khẩu của bạn"
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
  } = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    mode: "onSubmit",
  });

  const onSubmit = async (data: RegisterFormValues) => {
    try {
      const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          fullName: data.fullName,
          dateOfBirth: data.dateOfBirth,
          phoneNumber: data.phoneNumber,
          email: data.email,
          password: data.password,
          confirmPassword: data.confirmPassword
        }),
        credentials: "include"
      });

      const result = await res.json();
      console.log(result);

      if (!result.success) {
        alert(result.message || "Đăng ký thất bại");
        return;
      }

      router.push("/login");
    } catch (error) {
      console.error("Error:", error);
      alert("Có lỗi xảy ra khi đăng ký");
    }
  };

  // Lấy ngày hôm nay để giới hạn không được chọn ngày trong tương lai
  const today = new Date().toISOString().split('T')[0];

  return (
    <>
      <div className="w-full max-w-[800px] mx-auto px-4 sm:px-6 lg:px-8 py-[24px] md:py-[60px]">
        <h2 className="font-[700] text-[24px] sm:text-[28px] md:text-[33px] text-[#D70018] text-center mb-6 md:mb-[32px]">
          Đăng ký tài khoản để tiếp tục
        </h2>

        <div className="flex flex-col justify-center items-center gap-3 md:gap-[20px] mb-6 md:mb-[38px]">
          <p className="font-[400] text-[14px] md:text-[16px] text-[#71717a]">
            Đăng ký bằng tài khoản mạng xã hội
          </p>
          <div className="flex flex-col sm:flex-row gap-3 md:gap-[20px] w-full sm:w-auto">
            {socialButtons.map((social) => (
              <SocialButton
                key={social.label}
                icon={social.icon}
                label={social.label}
              />
            ))}
          </div>
        </div>

        <div className="">
          <p className="font-[400] text-[14px] md:text-[16px] text-[#71717a] text-center mb-4 md:mb-[16px]">
            Hoặc điền thông tin sau
          </p>
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="flex flex-col gap-4 mb-6 md:mb-[32px] w-full"
          >
            <p className="font-[700] text-[16px] md:text-[18px] text-[#121214]">
              Thông tin cá nhân
            </p>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-[24px] mb-3 md:mb-[14px]">
              {inputFieldInfos.map((field) => (
                <InputField
                  key={field.id}
                  label={field.label}
                  type={field.type}
                  id={field.id}
                  placeholder={field.placeholder}
                  max={field.id === "dateOfBirth" ? today : undefined}
                  {...register(field.id)}
                  error={errors[field.id]?.message}
                />
              ))}
            </div>

            <p className="font-[700] text-[16px] md:text-[18px] text-[#121214]">
              Tạo mật khẩu
            </p>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-[24px] mb-6 md:mb-[40px]">
              {inputFieldPasswords.map((field) => (
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
            </div>

            <div className="flex flex-col sm:flex-row gap-4 md:gap-[24px]">
              <Link
                className="border border-[#CFCFD3] flex justify-center items-center gap-2 w-full sm:flex-1 px-4 py-3 rounded-[8px] hover:bg-gray-50 transition-colors"
                href={"/login"}
              >
                <FaChevronLeft className="text-[14px] md:text-[16px]" />
                <p className="font-[500] text-[14px] md:text-[16px] color-[#1D1D20]">
                  Quay lại đăng nhập
                </p>
              </Link>
              <button
                type="submit"
                disabled={isSubmitting}
                className="border border-[#CFCFD3] bg-[#D70018] flex justify-center items-center gap-2 w-full sm:flex-1 px-4 py-3 rounded-[8px] hover:bg-[#b8001a] transition-colors disabled:opacity-60"
              >
                <span className="font-[500] text-[14px] md:text-[16px] text-[#fff]">
                  {isSubmitting ? "Đang xử lý..." : "Hoàn tất đăng ký"}
                </span>
              </button>
            </div>

          </form>
        </div>
      </div>
    </>
  )
}