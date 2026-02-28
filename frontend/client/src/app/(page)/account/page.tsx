import { BoxBreadcrumb } from '@/components/common/BoxBreadcrumb';
import { AccountContent } from './_components/AccountContent';

export default function AccountPage() {
  return (
    <>
      <BoxBreadcrumb
        title="Tài Khoản"
        list={[
          { title: 'Trang Chủ', link: '/' },
          { title: 'Tài Khoản', link: '/account' },
        ]}
      />

      <AccountContent />
    </>
  );
}
