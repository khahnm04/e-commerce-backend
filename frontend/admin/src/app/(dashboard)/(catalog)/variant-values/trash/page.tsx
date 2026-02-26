import { Metadata } from "next";
import VariantValueList from "../_components/VariantValueList";
import { Breadcrumb } from "@/components/common/Breadcrumb";

export const metadata: Metadata = {
  title: "Thùng rác - Giá trị biến thể",
  description: "Danh sách Giá trị biến thể đã xóa.",
};

export default function VariantValueTrashPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thùng rác - Giá trị biến thể
      </h1>
      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Giá trị biến thể", href: "/variant-values" },
          { label: "Thùng rác" },
        ]}
      />
      <VariantValueList isTrash={true} />
    </main>
  );
}
