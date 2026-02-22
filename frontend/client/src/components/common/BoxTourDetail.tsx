import { FaCalendarDays, FaCar, FaRegStar, FaStar, FaTicketSimple } from 'react-icons/fa6';

export const BoxTourDetail = () => {
  return (
    <div className="box-tour-detail bg-white shadow-[0px_1.75px_3.51px_0px_#0000001F] rounded-[10px] p-[15px] sm:p-[24px] sticky top-[80px] left-0">
      <div className="inner-title-main font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)] mb-[15px]">
        Chuyến Đi Của Bạn
      </div>

      <div className="inner-product flex items-center mb-[15px]">
        <div className="inner-image w-[103px] sm:w-[132px] aspect-[132/98] rounded-[10px] overflow-hidden mr-[12px] shrink-0">
          <img
            src="/assets/images/banner-12.jpg"
            alt="Tour Image"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="inner-info flex-1 min-w-0">
          <div className="inner-title font-normal text-[14px] sm:text-[16px] text-[var(--color-primary)] mb-[8px] overflow-hidden line-clamp-2">
            Hà Nội - Ninh Bình - Hạ Long - Yên Tử - Sapa | 6N5D
          </div>
          <div className="inner-rating flex items-center justify-between flex-wrap gap-[5px]">
            <div className="inner-stars inline-flex gap-[3px] text-[12px] sm:text-[15px] text-[#FFD600]">
              <FaStar />
              <FaStar />
              <FaStar />
              <FaStar />
              <FaRegStar />
            </div>
            <div className="inner-number font-light text-[10px] sm:text-[12px] text-[#1F1F1F]">
              500 lượt đánh giá
            </div>
          </div>
        </div>
      </div>

      <div className="inner-meta mb-[15px] flex flex-col gap-[8px]">
        <div className="inner-item flex items-center font-normal text-[14px] text-[#5D5E60]">
          <span className="text-[16px] mr-[8px]"><FaTicketSimple /></span>
          <span>Mã Tour:</span>
          <span className="inner-highlight text-[var(--color-primary)] ml-[5px]">28T00001</span>
        </div>
        <div className="inner-item flex items-center font-normal text-[14px] text-[#5D5E60]">
          <span className="text-[16px] mr-[8px]"><FaCalendarDays /></span>
          <span>Thời Gian:</span>
          <span className="inner-highlight text-[var(--color-primary)] ml-[5px]">6 Ngày 5 Đêm</span>
        </div>
        <div className="inner-item flex items-center font-normal text-[14px] text-[#5D5E60]">
          <span className="text-[16px] mr-[8px]"><FaCar /></span>
          <span>Phương Tiện:</span>
          <span className="inner-highlight text-[var(--color-primary)] ml-[5px]">Ô tô 45 chỗ</span>
        </div>
        <div className="inner-item flex items-center font-normal text-[14px] text-[#5D5E60]">
          <span className="text-[16px] mr-[8px]"><FaCalendarDays /></span>
          <span>Ngày Khởi Hành:</span>
          <span className="inner-highlight text-[var(--color-primary)] ml-[5px]">20/10/2024</span>
        </div>
      </div>

      <div className="inner-group mb-[15px]">
        <label className="inner-label block mb-[5px] font-normal text-[14px] text-[#5D5E60]">Khởi Hành Tại:</label>
        <select className="w-full h-[38px] border border-[#9EA0A3] rounded-[5px] px-[14px] outline-none">
          <option value="">Hà Nội</option>
          <option value="">Đà Nẵng</option>
          <option value="">Nha Trang</option>
        </select>
      </div>

      <div className="inner-group mb-[15px]">
        <label className="inner-label block mb-[5px] font-normal text-[14px] text-[#5D5E60]">
          Số Lượng Hành Khách
        </label>
        <div className="inner-list flex flex-col gap-[8px]">
          <div className="inner-item flex items-center">
            <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px]">
              Người lớn:
            </div>
            <input
              type="number"
              defaultValue="1"
              className="font-medium text-[14px] text-[#898B8D] w-[40px] border-0 border-b border-dotted border-[#848484] outline-none"
            />
            <div className="inner-price flex-1 text-right font-normal text-[16px] text-[#898B8D]">
              1 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
            </div>
          </div>
          <div className="inner-item flex items-center">
            <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px]">
              Trẻ em:
            </div>
            <input
              type="number"
              defaultValue="0"
              className="font-medium text-[14px] text-[#898B8D] w-[40px] border-0 border-b border-dotted border-[#848484] outline-none"
            />
            <div className="inner-price flex-1 text-right font-normal text-[16px] text-[#898B8D]">
              0 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
            </div>
          </div>
          <div className="inner-item flex items-center">
            <div className="inner-item-label font-normal text-[14px] text-[#5D5E60] w-[100px]">
              Em bé:
            </div>
            <input
              type="number"
              defaultValue="0"
              className="font-medium text-[14px] text-[#898B8D] w-[40px] border-0 border-b border-dotted border-[#848484] outline-none"
            />
            <div className="inner-price flex-1 text-right font-normal text-[16px] text-[#898B8D]">
              0 x <span className="inner-highlight text-[var(--color-primary)]">10.000.000</span>
            </div>
          </div>
        </div>
      </div>

      <div className="inner-total mb-[15px] sm:mb-[18px] flex items-center justify-between">
        <div className="inner-label font-normal text-[16px] text-[#5D5E60]">
          Tổng cộng:
        </div>
        <div className="inner-price font-semibold text-[20px] text-[var(--color-primary)]">
          10.000.000 đ
        </div>
      </div>

      <button className="inner-button-add-cart w-full h-[44px] border-0 bg-[var(--color-primary)] rounded-[10px] font-semibold text-[16px] text-white cursor-pointer hover:brightness-110 transition-all">
        Thêm Vào Giỏ Hàng
      </button>
    </div>
  );
};
