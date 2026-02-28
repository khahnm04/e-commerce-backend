import { BoxBreadcrumb } from '@/components/common/BoxBreadcrumb';
import { NewsListingContent } from './_components/NewsListingContent';

export default function NewsPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Tin Tức"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Tin Tức', link: '/news' },
        ]}
      />

      <NewsListingContent />
    </>
  );
}
