import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import AttributeForm from "../_components/AttributeForm";

export const metadata: Metadata = {
  title: "Thêm mới thuộc tính",
};

export default function AttributeCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới thuộc tính
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Thuộc tính", href: "/attributes" },
          { label: "Thêm mới" },
        ]}
      />

      <AttributeForm />
    </main>
  );
}
