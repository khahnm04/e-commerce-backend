import { BoxBreadcrumb } from '@/components/common/BoxBreadcrumb';
import { NewsDetailContent } from '../_components/NewsDetailContent';

export default function NewsDetailPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Chi Tiết Tin Tức"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Tin Tức', link: '/news' },
          { title: 'Chi Tiết Tin Tức', link: '#' },
        ]}
      />

      <NewsDetailContent />
    </>
  );
}
