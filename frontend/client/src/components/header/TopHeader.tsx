import Link from "next/link";
import { FaPhone, FaEnvelope, FaBuilding } from "react-icons/fa6";

const TOP_HEADER_ITEMS = [
  {
    icon: FaPhone,
    text: "1900 1099",
  },
  {
    icon: FaEnvelope,
    text: "contact@shoptech.com",
  },
  {
    icon: FaBuilding,
    text: "Tra cứu đơn hàng",
  },
];

export const TopHeader = () => {
  return (
    <div className="top-header bg-[var(--color-primary)] py-[10px] hidden md:block text-[#FFFBEB] text-[14px]">
      <div className="container">
        <div className="inner-wrap flex justify-between items-center">
          <div className="left-side">
            Xem giá tại: <span className="font-bold">Hà Nội</span>
          </div>
          <div className="right-side flex items-center gap-[20px]">
            {TOP_HEADER_ITEMS.map((item, index) => {
              const Icon = item.icon;
              return (
                <div
                  key={index}
                  className="inner-item inline-flex items-center gap-[8px]"
                >
                  <Icon className="text-[16px]" />
                  {item.text}
                </div>
              );
            })}
            <div className="h-[14px] w-[1px] bg-[#FFFBEB] opacity-50"></div>
            <div className="flex gap-[5px]">
              <Link href="/login" className="hover:opacity-80">
                Đăng nhập
              </Link>
              <span>/</span>
              <Link href="/register" className="hover:opacity-80">
                Đăng ký
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
