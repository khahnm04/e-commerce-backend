import { Metadata } from "next";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import ProductForm from "../_components/ProductForm";
import { MOCK_PRODUCT_LIST } from "@/data/mock-product";

export const metadata: Metadata = {
  title: "Chi tiết sản phẩm",
  description: "Trang chỉnh sửa thông tin chi tiết sản phẩm.",
};

export default async function ProductDetailPage({
  params,
}: {
  params: Promise<{ slug: string }>
}) {
  const { slug } = await params;

  // Tìm kiếm sản phẩm trong Mock Data
  const productData = MOCK_PRODUCT_LIST.find((item: any) => item.slug === slug);
  const name: string = productData?.name || "Chi tiết sản phẩm";

  if (!productData) {
    return (
      <div className="p-10 text-center">
        <h2 className="text-xl font-bold text-red-500 mb-2">Không tìm thấy sản phẩm!</h2>
        <p className="text-gray-500">Sản phẩm này có thể đã bị xóa hoặc đường dẫn không đúng.</p>
      </div>
    );
  }

  return (
    <main>
      <h1 className="mt-0 mb-[5px] font-bold text-[32px] text-[var(--color-text)] capitalize">
        {name}
      </h1>

      <Breadcrumb
        className="mb-[30px] ml-[5px]"
        items={[
          { label: "Tổng quan", href: "/" },
          { label: "Sản phẩm", href: "/products" },
          { label: name }
        ]}
      />

      {/* Truyền dữ liệu tìm được vào Form để kích hoạt chế độ Edit */}
      <ProductForm initialData={productData} />
    </main>
  );
}