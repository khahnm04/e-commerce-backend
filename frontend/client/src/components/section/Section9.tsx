import Link from 'next/link';
import { FaAngleRight, FaXmark } from 'react-icons/fa6';

export const Section9 = () => {
  return (
    <div className="section-9 py-[45px] sm:pt-[45px] pb-[30px] lg:pt-[60px] lg:pb-[30px]">
      <div className="container mx-auto px-4">
        <div className="inner-wrap w-full lg:w-[902px] bg-white shadow-[0px_3.05px_3.81px_0px_#00000026] rounded-[18px] p-[20px_15px] sm:p-[30px_20px] mx-auto">
          <div className="inner-head flex items-center justify-between mb-[25px]">
            <div className="inner-title font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)]">
              Giỏ Hàng
            </div>
            <Link href="#" className="inner-back font-normal text-[12px] sm:text-[15px] text-[#52575C] inline-flex items-center gap-[5px] hover:text-[var(--color-primary)]">
              Quay lại mua hàng <FaAngleRight className="text-[9px]" />
            </Link>
          </div>

          <div className="inner-tour-list">
            {[1, 2].map((item) => (
              <div key={item} className="inner-tour-item pb-[10px] sm:pb-[15px] border-b border-[#F2F2F2] mb-[15px] flex flex-wrap lg:flex-nowrap items-start">
                <div className="inner-actions mr-0 lg:mr-[6px] w-full lg:w-[21px] flex lg:block justify-between lg:justify-start mb-[10px] lg:mb-0">
                  <button className="inner-delete order-1 lg:order-none w-[21px] h-[21px] inline-flex items-center justify-center text-[16px] bg-transparent border-0 cursor-pointer text-[#828282] hover:text-red-500">
                    <FaXmark />
                  </button>
                  <input type="checkbox" className="inner-check w-[21px] h-[21px] mt-0 lg:mt-[43px] accent-[var(--color-primary)] cursor-pointer" />
                </div>

                <div className="inner-product w-full lg:w-[59%] flex items-start mb-[10px] lg:mb-0">
                  <div className="inner-image w-[118px] sm:w-[178px] aspect-[178/149] rounded-[8px] overflow-hidden mr-[10px] sm:mr-[15px] shrink-0">
                    <img src="/assets/images/banner-15.jpg" alt="Tour Image" className="w-full h-full object-cover" />
                  </div>
                  <div className="inner-content flex-1">
                    <div className="inner-title mb-[5px] sm:mb-[12px]">
                      <Link href="#" className="font-medium text-[14px] sm:text-[16px] text-[#25282B] overflow-hidden line-clamp-2">
                        Tour Châu Âu Đón Noel | 11N10D | Pháp – Thụy Sĩ – Ý
                      </Link>
                    </div>
                    <div className="inner-meta flex flex-col gap-[3px] sm:gap-[5px]">
                      <div className="inner-meta-item font-normal text-[12px] sm:text-[14px] text-[#8C8C8C]">
                        Mã Tour: <b className="font-semibold text-[#8C8C8C]">123456789</b>
                      </div>
                      <div className="inner-meta-item font-normal text-[12px] sm:text-[14px] text-[#8C8C8C]">
                        Ngày Khởi Hành: <b className="font-semibold text-[#8C8C8C]">20/10/2024</b>
                      </div>
                      <div className="inner-meta-item font-normal text-[12px] sm:text-[14px] text-[#8C8C8C]">
                        Khởi Hành Tại: <b className="font-semibold text-[#8C8C8C]">Hà Nội</b>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="inner-quantity flex-1 ml-0 lg:ml-[10px]">
                  <div className="inner-label font-normal text-[14px] text-[#5D5E60] mb-[8px] text-left lg:text-right">
                    Số Lượng Hành Khách
                  </div>
                  <div className="inner-list flex flex-col gap-[8px]">
                    <div className="inner-item flex items-center">
                      <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px] mr-[16px]">
                        Người lớn:
                      </div>
                      <div className="inner-item-input flex-1">
                        <input type="number" defaultValue="1" className="w-[40px] border-0 outline-none font-medium text-[14px] text-[#898B8D] border-b border-dotted border-[#ddd]" />
                      </div>
                      <div className="inner-item-price font-normal text-[16px] text-[#898B8D]">
                        1 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
                      </div>
                    </div>
                    <div className="inner-item flex items-center">
                      <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px] mr-[16px]">
                        Trẻ em:
                      </div>
                      <div className="inner-item-input flex-1">
                        <input type="number" defaultValue="0" className="w-[40px] border-0 outline-none font-medium text-[14px] text-[#898B8D] border-b border-dotted border-[#ddd]" />
                      </div>
                      <div className="inner-item-price font-normal text-[16px] text-[#898B8D]">
                        0 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
                      </div>
                    </div>
                    <div className="inner-item flex items-center">
                      <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px] mr-[16px]">
                        Em bé:
                      </div>
                      <div className="inner-item-input flex-1">
                        <input type="number" defaultValue="0" className="w-[40px] border-0 outline-none font-medium text-[14px] text-[#898B8D] border-b border-dotted border-[#ddd]" />
                      </div>
                      <div className="inner-item-price font-normal text-[16px] text-[#898B8D]">
                        0 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="inner-coupon mb-[15px] flex justify-end">
            <form action="" id="coupon-form" className="w-full lg:w-[429px] flex relative">
              <input
                id="coupon-input"
                name="coupon"
                type="text"
                placeholder="Nhập mã giảm giá"
                className="flex-1 h-[40px] rounded-l-[5px] border border-[#9EA0A3] font-normal text-[14px] text-[#333333] px-[12px] outline-none"
              />
              <button className="h-[40px] rounded-r-[5px] bg-[var(--color-primary)] px-[24px] border-0 font-normal text-[14px] text-white cursor-pointer">
                Dùng Mã
              </button>
            </form>
          </div>

          <div className="inner-list-price flex flex-col gap-[16px]">
            <div className="inner-item flex items-center justify-between">
              <div className="inner-label font-normal text-[14px] text-[#333333]">
                Tổng tiền:
              </div>
              <div className="inner-price font-normal text-[17px] text-[#333333]">
                15.070.000 đ
              </div>
            </div>
            <div className="inner-item flex items-center justify-between">
              <div className="inner-label font-normal text-[14px] text-[#333333]">
                Giảm:
              </div>
              <div className="inner-price font-normal text-[17px] text-[#333333]">
                -5.070.000 đ
              </div>
            </div>
            <div className="inner-item flex items-center justify-between">
              <div className="inner-label font-normal text-[14px] text-[#333333]">
                Thanh toán:
              </div>
              <div className="inner-price-highlight font-bold text-[22px] text-[var(--color-primary)]">
                17.070.000 đ
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
