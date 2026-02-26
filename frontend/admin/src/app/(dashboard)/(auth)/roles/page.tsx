import { Metadata } from "next";
import RoleList from "./_components/RoleList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Quản lý nhóm quyền",
  description: "Danh sách nhóm quyền",
};

export default function RolePage() {
  return (
    <main className="">
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý nhóm quyền
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhóm quyền" }
        ]}
      />

      <RoleList />
    </main>
  );
}
