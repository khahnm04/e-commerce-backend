import { FaFilter } from "react-icons/fa6";

export const BoxFilter = () => {
  return (
    <>
      <div className="box-filter bg-white rounded-[10px] p-[15px] shadow-[0px_0px_4px_0px_#00000040]">
        <div className="inner-head flex items-center justify-between mb-[15px]">
          <div className="inner-title font-semibold text-[18px] text-[var(--color-primary)]">
            Bộ Lọc
          </div>
          <FaFilter className="text-[20px] text-[var(--color-primary)]" />
        </div>
        <div className="inner-body flex flex-col gap-[15px]">
          <div className="inner-group">
            <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-[5px]">
              Điểm đi
            </div>
            <select
              name=""
              id=""
              className="w-full h-[38px] border border-[#898B8D] rounded-[5px] font-normal text-[14px] text-[#898B8D] px-[14px] outline-none"
            >
              <option value="">-- Chọn điểm đi --</option>
              <option value="">Hà Nội</option>
              <option value="">Đà Nẵng</option>
              <option value="">Nha Trang</option>
            </select>
          </div>
          <div className="inner-group">
            <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-[5px]">
              Điểm đến
            </div>
            <select
              name=""
              id=""
              className="w-full h-[38px] border border-[#898B8D] rounded-[5px] font-normal text-[14px] text-[#898B8D] px-[14px] outline-none"
            >
              <option value="">-- Chọn điểm đến --</option>
              <option value="">Hà Nội</option>
              <option value="">Đà Nẵng</option>
              <option value="">Nha Trang</option>
            </select>
          </div>
          <div className="inner-group">
            <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-[5px]">
              Ngày khởi hành
            </div>
            <input
              type="date"
              className="w-full h-[38px] border border-[#898B8D] rounded-[5px] font-normal text-[14px] text-[#898B8D] px-[14px] outline-none"
            />
          </div>
          <div className="inner-group">
            <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-[5px]">
              Số Lượng Hành Khách
            </div>
            <div className="inner-input-list flex flex-col gap-[5px]">
              <div className="inner-input-item border border-[#898B8D] rounded-[5px] h-[38px] flex items-center justify-between px-[16px]">
                <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-0 flex-1">
                  Người lớn:
                </div>
                <input
                  type="number"
                  defaultValue="0"
                  className="w-[45px] text-[14px] font-medium text-[#898B8D] border-0 outline-none"
                />
              </div>
              <div className="inner-input-item border border-[#898B8D] rounded-[5px] h-[38px] flex items-center justify-between px-[16px]">
                <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-0 flex-1">
                  Trẻ em:
                </div>
                <input
                  type="number"
                  defaultValue="0"
                  className="w-[45px] text-[14px] font-medium text-[#898B8D] border-0 outline-none"
                />
              </div>
              <div className="inner-input-item border border-[#898B8D] rounded-[5px] h-[38px] flex items-center justify-between px-[16px]">
                <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-0 flex-1">
                  Em bé:
                </div>
                <input
                  type="number"
                  defaultValue="0"
                  className="w-[45px] text-[14px] font-medium text-[#898B8D] border-0 outline-none"
                />
              </div>
            </div>
          </div>
          <div className="inner-group">
            <div className="inner-label font-normal text-[14px] text-[#898B8D] mb-[5px]">
              Mức giá
            </div>
            <select
              name=""
              id=""
              className="w-full h-[38px] border border-[#898B8D] rounded-[5px] font-normal text-[14px] text-[#898B8D] px-[14px] outline-none"
            >
              <option value="">-- Chọn khoảng giá --</option>
              <option value="">Dưới 1tr</option>
              <option value="">Từ 1tr đến 3tr</option>
              <option value="">Từ 3tr đến 6tr</option>
              <option value="">Từ 6tr đến 10tr</option>
              <option value="">Trên 10tr</option>
            </select>
          </div>
          <div className="inner-group">
            <button className="inner-button bg-[var(--color-primary)] rounded-[5px] h-[38px] w-full font-medium text-[14px] text-white border-0 cursor-pointer">
              Áp Dụng
            </button>
          </div>
        </div>
      </div>
      <div className="inner-overlay"></div>
    </>
  );
};
