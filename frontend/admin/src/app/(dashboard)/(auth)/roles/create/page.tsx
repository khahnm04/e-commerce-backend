import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import RoleForm from "../_components/RoleForm";

export const metadata: Metadata = {
  title: "Tạo mới nhóm quyền",
  description: "Tạo mới nhóm quyền",
};

export default function CreateRolePage() {
  return (
    <>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm nhóm quyền
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhóm quyền", href: "/roles" },
          { label: "Thêm mới" },
        ]}
      />

      <RoleForm />
    </>
  );
}
