import { BoxBreadcrumb } from "@/components/common/BoxBreadcrumb";
import { Section8 } from "@/components/section/Section8";

export default function TourDetailPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Tour Hà Nội - Ninh Bình - Hạ Long - Yên Tử - Sapa | 6N5D"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Tour Trong nước', link: '/tours/trong-nuoc' },
          { title: 'Tour Miền Bắc', link: '/tours/trong-nuoc/mien-bac' },
          { title: 'Tour Hà Nội - Ninh Bình - Hạ Long - Yên Tử - Sapa | 6N5D', link: '/tours/trong-nuoc/mien-bac' },
        ]}
      />

      <Section8 />
    </>
  )
}
