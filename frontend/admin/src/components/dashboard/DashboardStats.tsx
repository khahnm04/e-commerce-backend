/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable @next/next/no-img-element */

interface StatItem {
  image: string;
  name: string;
  number: string;
}

const DASHBOARD_STATS: StatItem[] = [
  {
    image: "assets/images/section-1-icon-1.svg",
    name: "Người dùng",
    number: "1.200",
  },
  {
    image: "assets/images/section-1-icon-2.svg",
    name: "Đơn hàng",
    number: "3.600",
  },
  {
    image: "assets/images/section-1-icon-3.svg",
    name: "Doanh thu",
    number: "300.000.000đ",
  },
];

export const DashboardStats = () => {
  return (
    <div className="grid xl:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-[30px]">
      {DASHBOARD_STATS.map((item, index) => (
        <div
          key={index}
          className="inner-item bg-white rounded-[14px] p-[28px] flex items-center justify-center gap-[20px]"
        >
          <div className="inner-icon w-[60px]">
            <img
              src={item.image}
              className="w-full h-auto"
            />
          </div>
          <div className="inner-content">
            <div className="inner-title font-[600] text-[16px] text-[var(--color-text)] mb-[4px]">
              {item.name}
            </div>
            <div className="inner-number font-[700] text-[28px] text-[var(--color-text)]">
              {item.number}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};
