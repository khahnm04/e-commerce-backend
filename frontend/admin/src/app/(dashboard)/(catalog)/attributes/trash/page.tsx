import { Metadata } from "next";
import AttributeList from "../_components/AttributeList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Thuộc tính",
  description: "Danh sách Thuộc tính đã xóa.",
};

export default function AttributeTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác - Thuộc tính
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Thuộc tính", href: "/attributes" },
          { label: "Thùng rác" },
        ]}
      />
      <AttributeList isTrash={true} />
    </main>
  );
}
