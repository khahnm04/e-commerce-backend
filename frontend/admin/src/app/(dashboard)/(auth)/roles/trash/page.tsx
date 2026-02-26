import { Metadata } from "next";
import RoleList from "../_components/RoleList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Nhóm quyền",
  description: "Danh sách nhóm quyền đã xóa.",
};

export default function RoleTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác nhóm quyền
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Nhóm quyền", href: "/roles" },
          { label: "Thùng rác" },
        ]}
      />

      <RoleList isTrash={true} />
    </main>
  );
}
