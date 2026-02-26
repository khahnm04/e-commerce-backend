/* eslint-disable jsx-a11y/alt-text */
/* eslint-disable @next/next/no-img-element */

const TABLE_HEADERS = [
  "Mã",
  "Thông tin khách",
  "Danh sách tour",
  "Thanh toán",
  "Trạng thái",
  "Ngày đặt",
];

const getHeaderClass = (index: number, total: number) => {
  const baseClass = "bg-[#F1F4F9] px-[24px] py-[14px] font-bold text-[14px] text-[var(--color-text)]";
  const firstClass = index === 0 ? "rounded-l-[12px] text-left" : "text-left";
  const lastClass = index === total - 1 ? "rounded-r-[12px] text-right" : "";
  return `${baseClass} ${firstClass} ${lastClass}`;
};

export const RecentOrdersTable = () => {
  return (
    <div className="mt-[30px] bg-white shadow-[6px_6px_54px_0px_#0000000D] rounded-[14px] p-[32px]">
      <h2 className="font-[600] text-[24px] text-[var(--color-text)] mt-0 mb-[30px]">
        Đơn hàng mới
      </h2>
      <div className="overflow-x-auto">
        <table className="w-full border-collapse min-w-[1076px]">
          <thead>
            <tr>
              {TABLE_HEADERS.map((item, index) => (
                <th
                  key={index}
                  className={getHeaderClass(index, TABLE_HEADERS.length)}
                >
                  {item}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            <tr>
              <td className="px-[24px] py-[10px] text-left border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)]">
                <div className="font-[700] text-[14px] text-[var(--color-primary)]">OD000001</div>
              </td>
              <td className="px-[24px] py-[10px] text-left border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)]">
                <div>Họ tên: Lê Văn A</div>
                <div>SĐT: 0123456789</div>
                <div>Ghi chú: Test...</div>
              </td>
              <td className="px-[24px] py-[10px] text-left border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)]">
                <div className="flex flex-col gap-[10px]">
                  <div className="flex items-start gap-[10px]">
                    <div className="w-[76px] h-[76px] rounded-[6px] overflow-hidden">
                      <img
                        src="assets/images/tour-1.jpg"
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <div className="">
                      <div className="font-[600] text-[14px] text-[var(--color-text)] mb-[3px]">
                        Tour Hạ Long
                      </div>
                      <div className="flex flex-col gap-[3px] font-[600] text-[12px] text-[var(--color-text)]">
                        <div>Người lớn: 3 x 1.500.000đ</div>
                        <div>Trẻ em: 2 x 1.300.000đ</div>
                        <div>Em bé: 2 x 1.000.000đ</div>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-start gap-[10px]">
                    <div className="w-[76px] h-[76px] rounded-[6px] overflow-hidden">
                      <img
                        src="assets/images/tour-1.jpg"
                        className="w-full h-full object-cover"
                      />
                    </div>
                    <div className="">
                      <div className="font-[600] text-[14px] text-[var(--color-text)] mb-[3px]">
                        Tour Hạ Long
                      </div>
                      <div className="flex flex-col gap-[3px] font-[600] text-[12px] text-[var(--color-text)]">
                        <div>Người lớn: 3 x 1.500.000đ</div>
                        <div>Trẻ em: 2 x 1.300.000đ</div>
                        <div>Em bé: 2 x 1.000.000đ</div>
                      </div>
                    </div>
                  </div>
                </div>
              </td>
              <td className="px-[24px] py-[10px] text-left border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)]">
                <div>Tổng tiền: 10.000.000đ</div>
                <div>Giảm: 400.000đ</div>
                <div>Mã giảm: TOURMUAHE2024</div>
                <div>Thanh toán: 9.600.000đ</div>
                <div>PTTT: Ví Momo</div>
                <div>TTTT: Đã thanh toán</div>
              </td>
              <td className="px-[24px] py-[10px] text-left border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)]">
                <div className="badge badge-orange">
                  Khởi tạo
                </div>
              </td>
              <td className="px-[24px] py-[10px] border-b border-[#97979751] font-[600] text-[14px] text-[var(--color-text)] text-right">
                <div>16:20</div>
                <div>01/01/2024</div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};
