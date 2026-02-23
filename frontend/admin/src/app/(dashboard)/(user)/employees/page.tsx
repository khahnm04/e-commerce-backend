import { Metadata } from "next";
import EmployeeList from "./_components/EmployeeList";
import { Pagination } from "@/components/common/Pagination";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý nhân viên",
  description: "Danh sách nhân viên, bộ lọc và tìm kiếm.",
};

export default function EmployeePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý nhân viên
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhân viên" }
        ]}
      />

      <EmployeeList />

      <Pagination />
    </main>
  );
}
