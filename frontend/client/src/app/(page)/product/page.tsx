import { BoxBreadcrumb } from "@/components/common/BoxBreadcrumb";
import { Section7 } from "@/components/section/Section7";

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

      <Section7 />
    </>
  )
}
