/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
import { Metadata } from "next"
import FormLogin from "./_components/FormLogin";

export const metadata: Metadata = {
  title: "Đăng nhập",
  description: "Đăng nhập hệ thống quản trị.",
}

export default function LoginPage() {
  return (
    <>
      <div className="bg-[url('/assets/images/bg-account.svg')] flex items-center justify-center min-h-[100vh] bg-cover bg-no-repeat bg-center">
        <div className="bg-[#fff] rounded-[19.2px] md:w-[504px] w-[95%] mx-auto md:py-[72px] py-[32px] md:px-[45.6px] px-[16px]">
          <h2 className="font-[700] text-[25.6px] text-[var(--color-text)] text-center mt-[0] mb-[12px]">
            Đăng nhập
          </h2>
          <p className="font-[600] text-[14.4px] text-[var(--color-text)] opacity-[0.8] text-center mt-0 mb-[32px]">
            Đăng nhập hệ thống quản trị
          </p>
          <FormLogin />
        </div>
      </div>
    </>
  )
}
