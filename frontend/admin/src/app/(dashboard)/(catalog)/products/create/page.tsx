import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import ProductForm from "../_components/ProductForm";

export const metadata: Metadata = {
  title: "Tạo mới sản phẩm",
  description: "Trang thêm mới sản phẩm vào hệ thống.",
};

export default function CreateProductPage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-bold text-[32px] text-[var(--color-text)]">Tạo sản phẩm</h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm", href: "/products" },
          { label: "Tạo mới" },
        ]}
      />

      <ProductForm />
    </main>
  );
}
