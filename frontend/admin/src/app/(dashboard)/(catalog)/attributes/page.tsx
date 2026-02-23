import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import AttributeList from "./_components/AttributeList";

export const metadata: Metadata = {
  title: "Quản lý thuộc tính",
  description: "Quản lý thuộc tính",
};

export default function AttributePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Quản lý thuộc tính
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[{
          label: "Tổng quan",
          href: "/"
        },
        {
          label: "Thuộc tính"
        }]}
      />

      <AttributeList />
    </main>
  );
}
