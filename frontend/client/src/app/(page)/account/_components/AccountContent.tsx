'use client';

import { useState, useCallback } from 'react';
import Link from 'next/link';
import {
  FaBell,
  FaBellSlash,
  FaBoxOpen,
  FaGear,
  FaGlobe,
  FaHeart,
  FaKey,
  FaLocationDot,
  FaPen,
  FaPlus,
  FaRightFromBracket,
  FaTrashCan,
  FaUser,
  FaXmark,
} from 'react-icons/fa6';
import { IconType } from 'react-icons';

/* ------------------------------------------------------------------ */
/* Types                                                              */
/* ------------------------------------------------------------------ */

interface MenuItem {
  key: string;
  label: string;
  icon: IconType;
}

interface OrderItem {
  id: string;
  date: string;
  total: string;
  status: string;
  statusColor: string;
}

interface WishlistItem {
  id: number;
  title: string;
  image: string;
  price: string;
  oldPrice?: string;
}

interface AddressItem {
  id: number;
  name: string;
  phone: string;
  address: string;
  isDefault: boolean;
}

interface SettingToggle {
  key: string;
  label: string;
  description: string;
  icon: IconType;
  defaultValue: boolean;
}

/* ------------------------------------------------------------------ */
/* Constants                                                          */
/* ------------------------------------------------------------------ */

const MENU_ITEMS: MenuItem[] = [
  { key: 'profile', label: 'Thông tin cá nhân', icon: FaUser },
  { key: 'orders', label: 'Đơn hàng của tôi', icon: FaBoxOpen },
  { key: 'wishlist', label: 'Sản phẩm yêu thích', icon: FaHeart },
  { key: 'addresses', label: 'Sổ địa chỉ', icon: FaLocationDot },
  { key: 'password', label: 'Đổi mật khẩu', icon: FaKey },
  { key: 'settings', label: 'Cài đặt', icon: FaGear },
];

const MOCK_ORDERS: OrderItem[] = [
  { id: '#DH001234', date: '25/02/2026', total: '15.070.000 đ', status: 'Đang xử lý', statusColor: '#F59E0B' },
  { id: '#DH001233', date: '20/02/2026', total: '2.590.000 đ', status: 'Đã giao', statusColor: '#10B981' },
  { id: '#DH001232', date: '15/02/2026', total: '8.900.000 đ', status: 'Đã hủy', statusColor: '#EF4444' },
  { id: '#DH001231', date: '10/02/2026', total: '12.350.000 đ', status: 'Đã giao', statusColor: '#10B981' },
];

const MOCK_WISHLIST: WishlistItem[] = [
  { id: 1, title: 'Tour Châu Âu Đón Noel | 11N10D | Pháp – Thụy Sĩ – Ý', image: '/assets/images/banner-15.jpg', price: '25.990.000 đ', oldPrice: '32.500.000 đ' },
  { id: 2, title: 'Hà Nội – Lào Cai – SaPa 4N3Đ', image: '/assets/images/product-1.jpg', price: '2.590.000 đ', oldPrice: '3.650.000 đ' },
  { id: 3, title: 'Tour Singapore – Malaysia 5N4Đ', image: '/assets/images/product-2.jpg', price: '8.900.000 đ' },
  { id: 4, title: 'Phú Quốc 3N2Đ – Nghỉ dưỡng Resort 5 sao', image: '/assets/images/product-3.jpg', price: '4.290.000 đ', oldPrice: '5.800.000 đ' },
];

const MOCK_ADDRESSES: AddressItem[] = [
  { id: 1, name: 'Nguyễn Văn A', phone: '0901 234 567', address: '123 Nguyễn Huệ, Phường Bến Nghé, Quận 1, TP. Hồ Chí Minh', isDefault: true },
  { id: 2, name: 'Nguyễn Văn A', phone: '0901 234 567', address: '456 Lê Lợi, Phường Bến Thành, Quận 1, TP. Hồ Chí Minh', isDefault: false },
];

const SETTINGS: SettingToggle[] = [
  { key: 'email_notifications', label: 'Thông báo qua Email', description: 'Nhận thông báo về đơn hàng, khuyến mại qua email', icon: FaBell, defaultValue: true },
  { key: 'sms_notifications', label: 'Thông báo qua SMS', description: 'Nhận tin nhắn SMS khi có cập nhật đơn hàng', icon: FaBellSlash, defaultValue: false },
  { key: 'promo_notifications', label: 'Nhận khuyến mại', description: 'Nhận thông tin về chương trình khuyến mại và ưu đãi đặc biệt', icon: FaHeart, defaultValue: true },
  { key: 'language', label: 'Ngôn ngữ', description: 'Chọn ngôn ngữ hiển thị cho giao diện', icon: FaGlobe, defaultValue: true },
];

/* ------------------------------------------------------------------ */
/* Sub-components                                                     */
/* ------------------------------------------------------------------ */

const SectionTitle = ({ children }: { children: React.ReactNode }) => (
  <h2 className="m-0 font-bold text-[20px] sm:text-[22px] text-[var(--color-primary)] mb-[24px]">
    {children}
  </h2>
);

const Sidebar = ({
  items,
  activeKey,
  onSelect,
}: {
  items: MenuItem[];
  activeKey: string;
  onSelect: (key: string) => void;
}) => (
  <aside className="w-full lg:w-[280px] shrink-0">
    <div className="bg-white rounded-[12px] shadow-[0px_1px_6px_rgba(0,0,0,0.08)] p-[20px] mb-[16px] flex items-center gap-[14px]">
      <div className="w-[56px] h-[56px] rounded-full bg-[var(--color-primary)]/10 inline-flex items-center justify-center shrink-0">
        <FaUser className="text-[22px] text-[var(--color-primary)]" />
      </div>
      <div>
        <p className="m-0 font-semibold text-[16px] text-[#25282B]">Nguyễn Văn A</p>
        <p className="m-0 font-normal text-[13px] text-[#A6A6A6]">nguyenvana@email.com</p>
      </div>
    </div>
    <div className="bg-white rounded-[12px] shadow-[0px_1px_6px_rgba(0,0,0,0.08)] overflow-hidden">
      <nav>
        <ul className="list-none m-0 p-0">
          {items.map(({ key, label, icon: Icon }) => (
            <li key={key}>
              <button
                type="button"
                onClick={() => onSelect(key)}
                className={`w-full flex items-center gap-[12px] px-[20px] py-[14px] border-0 cursor-pointer font-medium text-[14px] transition-all ${activeKey === key
                    ? 'bg-[var(--color-primary)]/10 text-[var(--color-primary)] border-l-[3px] border-l-[var(--color-primary)]'
                    : 'bg-transparent text-[#52575C] hover:bg-[#F9F9F9] border-l-[3px] border-l-transparent'
                  }`}
              >
                <Icon className="text-[16px]" />
                {label}
              </button>
            </li>
          ))}
          <li>
            <Link
              href="/login"
              className="w-full flex items-center gap-[12px] px-[20px] py-[14px] font-medium text-[14px] text-[#EF4444] hover:bg-red-50 transition-all border-l-[3px] border-l-transparent"
            >
              <FaRightFromBracket className="text-[16px]" />
              Đăng xuất
            </Link>
          </li>
        </ul>
      </nav>
    </div>
  </aside>
);

const ProfileTab = () => (
  <div>
    <SectionTitle>Thông Tin Cá Nhân</SectionTitle>
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-[16px]">
      {[
        { label: 'Họ và tên', value: 'Nguyễn Văn A', id: 'profile-name' },
        { label: 'Email', value: 'nguyenvana@email.com', id: 'profile-email' },
        { label: 'Số điện thoại', value: '0901 234 567', id: 'profile-phone' },
        { label: 'Ngày sinh', value: '01/01/1990', id: 'profile-birthday' },
      ].map(({ label, value, id }) => (
        <div key={id}>
          <label htmlFor={id} className="block font-medium text-[13px] text-[#A6A6A6] mb-[6px]">
            {label}
          </label>
          <input
            id={id}
            type="text"
            defaultValue={value}
            className="w-full h-[46px] px-[16px] border border-[#E0E0E0] rounded-[8px] outline-none font-normal text-[14px] text-[#333] focus:border-[var(--color-primary)] transition-colors"
          />
        </div>
      ))}
      <div className="col-span-1 sm:col-span-2">
        <label htmlFor="profile-address" className="block font-medium text-[13px] text-[#A6A6A6] mb-[6px]">
          Địa chỉ
        </label>
        <input
          id="profile-address"
          type="text"
          defaultValue="123 Nguyễn Huệ, Quận 1, TP.HCM"
          className="w-full h-[46px] px-[16px] border border-[#E0E0E0] rounded-[8px] outline-none font-normal text-[14px] text-[#333] focus:border-[var(--color-primary)] transition-colors"
        />
      </div>
    </div>
    <div className="mt-[24px]">
      <button
        type="button"
        className="px-[32px] h-[44px] bg-[var(--color-primary)] rounded-[8px] border-0 font-semibold text-[14px] text-white cursor-pointer hover:brightness-110 transition-all"
      >
        Lưu Thay Đổi
      </button>
    </div>
  </div>
);

const OrdersTab = ({ orders }: { orders: OrderItem[] }) => (
  <div>
    <SectionTitle>Đơn Hàng Của Tôi</SectionTitle>
    {/* Desktop table */}
    <div className="hidden sm:block overflow-x-auto">
      <table className="w-full border-collapse">
        <thead>
          <tr className="bg-[#F9F9F9]">
            {['Mã đơn', 'Ngày đặt', 'Tổng tiền', 'Trạng thái', ''].map((header) => (
              <th
                key={header || 'action'}
                className="px-[16px] py-[12px] text-left font-semibold text-[13px] text-[#52575C] first:rounded-l-[8px] last:rounded-r-[8px]"
              >
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id} className="border-b border-[#F2F2F2] last:border-b-0">
              <td className="px-[16px] py-[14px] font-semibold text-[14px] text-[var(--color-primary)]">{order.id}</td>
              <td className="px-[16px] py-[14px] font-normal text-[14px] text-[#52575C]">{order.date}</td>
              <td className="px-[16px] py-[14px] font-semibold text-[14px] text-[#25282B]">{order.total}</td>
              <td className="px-[16px] py-[14px]">
                <span
                  className="inline-block px-[10px] py-[4px] rounded-[4px] font-medium text-[12px] text-white"
                  style={{ backgroundColor: order.statusColor }}
                >
                  {order.status}
                </span>
              </td>
              <td className="px-[16px] py-[14px]">
                <Link href="#" className="font-medium text-[13px] text-[var(--color-primary)] hover:underline">
                  Chi tiết
                </Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    {/* Mobile cards */}
    <div className="sm:hidden flex flex-col gap-[12px]">
      {orders.map((order) => (
        <div key={order.id} className="bg-[#F9F9F9] rounded-[10px] p-[14px] flex flex-col gap-[8px]">
          <div className="flex items-center justify-between">
            <span className="font-semibold text-[14px] text-[var(--color-primary)]">{order.id}</span>
            <span
              className="inline-block px-[10px] py-[3px] rounded-[4px] font-medium text-[11px] text-white"
              style={{ backgroundColor: order.statusColor }}
            >
              {order.status}
            </span>
          </div>
          <div className="flex items-center justify-between">
            <span className="font-normal text-[13px] text-[#A6A6A6]">{order.date}</span>
            <span className="font-semibold text-[14px] text-[#25282B]">{order.total}</span>
          </div>
          <Link href="#" className="font-medium text-[13px] text-[var(--color-primary)]">
            Xem chi tiết →
          </Link>
        </div>
      ))}
    </div>
  </div>
);

const WishlistTab = ({
  items,
  onRemove,
}: {
  items: WishlistItem[];
  onRemove: (id: number) => void;
}) => (
  <div>
    <div className="flex items-center justify-between mb-[24px]">
      <SectionTitle>Sản Phẩm Yêu Thích</SectionTitle>
      <span className="font-normal text-[14px] text-[#A6A6A6]">{items.length} sản phẩm</span>
    </div>
    {items.length > 0 ? (
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-[16px]">
        {items.map((item) => (
          <div
            key={item.id}
            className="flex gap-[12px] bg-[#FAFAFA] rounded-[10px] p-[12px] group hover:shadow-[0px_2px_8px_rgba(0,0,0,0.08)] transition-all"
          >
            <Link href="/products" className="w-[100px] h-[80px] rounded-[8px] overflow-hidden shrink-0">
              <img src={item.image} alt={item.title} className="w-full h-full object-cover" />
            </Link>
            <div className="flex-1 min-w-0">
              <Link
                href="/products"
                className="block font-medium text-[14px] text-[#25282B] line-clamp-2 leading-[140%] mb-[6px] hover:text-[var(--color-primary)] transition-colors"
              >
                {item.title}
              </Link>
              <div className="flex items-center gap-[8px]">
                <span className="font-bold text-[15px] text-[var(--color-primary)]">{item.price}</span>
                {item.oldPrice && (
                  <span className="font-normal text-[13px] text-[#A6A6A6] line-through">{item.oldPrice}</span>
                )}
              </div>
            </div>
            <button
              type="button"
              onClick={() => onRemove(item.id)}
              aria-label="Xóa khỏi yêu thích"
              className="w-[32px] h-[32px] rounded-full bg-transparent border border-[#E0E0E0] inline-flex items-center justify-center cursor-pointer text-[#A6A6A6] hover:bg-[#FEE2E2] hover:text-[#EF4444] hover:border-[#FEE2E2] transition-all shrink-0 self-start"
            >
              <FaXmark className="text-[12px]" />
            </button>
          </div>
        ))}
      </div>
    ) : (
      <div className="text-center py-[40px]">
        <FaHeart className="text-[40px] text-[#E0E0E0] mx-auto mb-[12px]" />
        <p className="m-0 text-[14px] text-[#A6A6A6]">Chưa có sản phẩm yêu thích nào.</p>
      </div>
    )}
  </div>
);

const AddressesTab = ({
  addresses,
  onRemove,
}: {
  addresses: AddressItem[];
  onRemove: (id: number) => void;
}) => (
  <div>
    <div className="flex items-center justify-between mb-[24px]">
      <SectionTitle>Sổ Địa Chỉ</SectionTitle>
      <button
        type="button"
        className="inline-flex items-center gap-[6px] px-[16px] h-[38px] bg-[var(--color-primary)] rounded-[8px] border-0 font-medium text-[13px] text-white cursor-pointer hover:brightness-110 transition-all"
      >
        <FaPlus className="text-[11px]" /> Thêm địa chỉ
      </button>
    </div>
    {addresses.length > 0 ? (
      <div className="flex flex-col gap-[12px]">
        {addresses.map((addr) => (
          <div
            key={addr.id}
            className={`rounded-[10px] p-[16px] border-2 transition-all ${addr.isDefault
                ? 'border-[var(--color-primary)] bg-[var(--color-primary)]/5'
                : 'border-[#E0E0E0] bg-[#FAFAFA]'
              }`}
          >
            <div className="flex items-start justify-between mb-[8px]">
              <div className="flex items-center gap-[8px]">
                <span className="font-semibold text-[15px] text-[#25282B]">{addr.name}</span>
                <span className="text-[13px] text-[#A6A6A6]">|</span>
                <span className="font-normal text-[14px] text-[#52575C]">{addr.phone}</span>
                {addr.isDefault && (
                  <span className="inline-block px-[8px] py-[2px] bg-[var(--color-primary)] text-white text-[11px] font-medium rounded-[4px]">
                    Mặc định
                  </span>
                )}
              </div>
              <div className="flex items-center gap-[8px]">
                <button
                  type="button"
                  aria-label="Sửa địa chỉ"
                  className="w-[30px] h-[30px] rounded-[6px] bg-transparent border border-[#E0E0E0] inline-flex items-center justify-center cursor-pointer text-[#52575C] hover:border-[var(--color-primary)] hover:text-[var(--color-primary)] transition-all"
                >
                  <FaPen className="text-[11px]" />
                </button>
                {!addr.isDefault && (
                  <button
                    type="button"
                    onClick={() => onRemove(addr.id)}
                    aria-label="Xóa địa chỉ"
                    className="w-[30px] h-[30px] rounded-[6px] bg-transparent border border-[#E0E0E0] inline-flex items-center justify-center cursor-pointer text-[#52575C] hover:border-[#EF4444] hover:text-[#EF4444] transition-all"
                  >
                    <FaTrashCan className="text-[11px]" />
                  </button>
                )}
              </div>
            </div>
            <p className="m-0 font-normal text-[14px] text-[#747578] flex items-start gap-[6px]">
              <FaLocationDot className="text-[13px] mt-[3px] shrink-0 text-[#A6A6A6]" />
              {addr.address}
            </p>
          </div>
        ))}
      </div>
    ) : (
      <div className="text-center py-[40px]">
        <FaLocationDot className="text-[40px] text-[#E0E0E0] mx-auto mb-[12px]" />
        <p className="m-0 text-[14px] text-[#A6A6A6]">Chưa có địa chỉ nào được lưu.</p>
      </div>
    )}
  </div>
);

const ChangePasswordTab = () => (
  <div>
    <SectionTitle>Đổi Mật Khẩu</SectionTitle>
    <div className="max-w-[400px] flex flex-col gap-[16px]">
      {[
        { label: 'Mật khẩu hiện tại', id: 'current-password' },
        { label: 'Mật khẩu mới', id: 'new-password' },
        { label: 'Xác nhận mật khẩu mới', id: 'confirm-password' },
      ].map(({ label, id }) => (
        <div key={id}>
          <label htmlFor={id} className="block font-medium text-[13px] text-[#A6A6A6] mb-[6px]">
            {label}
          </label>
          <input
            id={id}
            type="password"
            placeholder={label}
            className="w-full h-[46px] px-[16px] border border-[#E0E0E0] rounded-[8px] outline-none font-normal text-[14px] text-[#333] focus:border-[var(--color-primary)] transition-colors"
          />
        </div>
      ))}
    </div>
    <div className="mt-[24px]">
      <button
        type="button"
        className="px-[32px] h-[44px] bg-[var(--color-primary)] rounded-[8px] border-0 font-semibold text-[14px] text-white cursor-pointer hover:brightness-110 transition-all"
      >
        Cập Nhật Mật Khẩu
      </button>
    </div>
  </div>
);

const SettingsTab = ({
  settings,
  toggleStates,
  onToggle,
}: {
  settings: SettingToggle[];
  toggleStates: Record<string, boolean>;
  onToggle: (key: string) => void;
}) => (
  <div>
    <SectionTitle>Cài Đặt</SectionTitle>
    <div className="flex flex-col gap-[4px]">
      {settings.map(({ key, label, description, icon: Icon }) => (
        <div
          key={key}
          className="flex items-center justify-between p-[16px] rounded-[10px] hover:bg-[#FAFAFA] transition-colors"
        >
          <div className="flex items-start gap-[14px] flex-1 mr-[16px]">
            <div className="w-[40px] h-[40px] rounded-[10px] bg-[var(--color-primary)]/10 inline-flex items-center justify-center shrink-0 mt-[2px]">
              <Icon className="text-[16px] text-[var(--color-primary)]" />
            </div>
            <div>
              <p className="m-0 font-medium text-[15px] text-[#25282B] mb-[2px]">{label}</p>
              <p className="m-0 font-normal text-[13px] text-[#A6A6A6]">{description}</p>
            </div>
          </div>
          <button
            type="button"
            onClick={() => onToggle(key)}
            aria-label={`${toggleStates[key] ? 'Tắt' : 'Bật'} ${label}`}
            className={`w-[48px] h-[26px] rounded-full border-0 cursor-pointer transition-all relative shrink-0 ${toggleStates[key]
                ? 'bg-[var(--color-primary)]'
                : 'bg-[#D1D5DB]'
              }`}
          >
            <span
              className={`absolute top-[3px] w-[20px] h-[20px] rounded-full bg-white shadow-[0px_1px_3px_rgba(0,0,0,0.15)] transition-all ${toggleStates[key] ? 'left-[25px]' : 'left-[3px]'
                }`}
            />
          </button>
        </div>
      ))}
    </div>
    <div className="mt-[20px] pt-[20px] border-t border-[#ECECEC]">
      <p className="m-0 font-normal text-[13px] text-[#A6A6A6]">
        Các thay đổi cài đặt sẽ được tự động lưu.
      </p>
    </div>
  </div>
);

/* ------------------------------------------------------------------ */
/* Main component                                                     */
/* ------------------------------------------------------------------ */

export const AccountContent = () => {
  const [activeTab, setActiveTab] = useState('profile');
  const [wishlist, setWishlist] = useState<WishlistItem[]>(MOCK_WISHLIST);
  const [addresses, setAddresses] = useState<AddressItem[]>(MOCK_ADDRESSES);
  const [toggleStates, setToggleStates] = useState<Record<string, boolean>>(
    Object.fromEntries(SETTINGS.map((s) => [s.key, s.defaultValue])),
  );

  const handleTabChange = useCallback((key: string) => {
    setActiveTab(key);
  }, []);

  const handleRemoveWishlist = useCallback((id: number) => {
    setWishlist((prev) => prev.filter((item) => item.id !== id));
  }, []);

  const handleRemoveAddress = useCallback((id: number) => {
    setAddresses((prev) => prev.filter((addr) => addr.id !== id));
  }, []);

  const handleToggleSetting = useCallback((key: string) => {
    setToggleStates((prev) => ({ ...prev, [key]: !prev[key] }));
  }, []);

  const renderContent = () => {
    switch (activeTab) {
      case 'profile':
        return <ProfileTab />;
      case 'orders':
        return <OrdersTab orders={MOCK_ORDERS} />;
      case 'wishlist':
        return <WishlistTab items={wishlist} onRemove={handleRemoveWishlist} />;
      case 'addresses':
        return <AddressesTab addresses={addresses} onRemove={handleRemoveAddress} />;
      case 'password':
        return <ChangePasswordTab />;
      case 'settings':
        return <SettingsTab settings={SETTINGS} toggleStates={toggleStates} onToggle={handleToggleSetting} />;
      default:
        return <ProfileTab />;
    }
  };

  return (
    <section className="account-page py-[30px] sm:py-[45px]">
      <div className="container mx-auto px-4">
        <div className="flex flex-col lg:flex-row gap-[20px]">
          <Sidebar
            items={MENU_ITEMS}
            activeKey={activeTab}
            onSelect={handleTabChange}
          />
          <div className="flex-1 bg-white rounded-[12px] shadow-[0px_1px_6px_rgba(0,0,0,0.08)] p-[20px] sm:p-[30px]">
            {renderContent()}
          </div>
        </div>
      </div>
    </section>
  );
};
