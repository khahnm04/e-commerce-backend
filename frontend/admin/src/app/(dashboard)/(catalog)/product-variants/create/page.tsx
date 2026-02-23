import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import ProductVariantForm from "../_components/ProductVariantForm";

export const metadata: Metadata = {
  title: "Thêm mới biến thể sản phẩm",
};

export default function ProductVariantCreatePage() {
  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
        Thêm mới biến thể sản phẩm
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Biến thể sản phẩm", href: "/product-variants" },
          { label: "Thêm mới" },
        ]}
      />

      <ProductVariantForm />
    </main>
  );
}
