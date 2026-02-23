import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import EmployeeForm from "../_components/EmployeeForm";

export const metadata: Metadata = {
  title: "Thêm mới nhân viên",
};

export default function EmployeeCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới nhân viên
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhân viên", href: "/employees" },
          { label: "Thêm mới" },
        ]}
      />

      <EmployeeForm />
    </main>
  );
}
