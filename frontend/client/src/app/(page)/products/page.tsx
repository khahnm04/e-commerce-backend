import { BoxBreadcrumb } from "@/components/common/BoxBreadcrumb";
import { ProductListingContent } from "@/app/(page)/products/_components/ProductListingContent";

export default function TourListPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Tour Nước Ngoài"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Tour Nước Ngoài', link: '/tours/nuoc-ngoai' }
        ]}
      />

      <ProductListingContent />
    </>
  )
}
