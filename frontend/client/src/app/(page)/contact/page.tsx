import { BoxBreadcrumb } from '@/components/common/BoxBreadcrumb';
import { ContactContent } from './_components/ContactContent';

export default function ContactPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Liên Hệ"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Liên Hệ', link: '/contact' },
        ]}
      />

      <ContactContent />
    </>
  );
}
