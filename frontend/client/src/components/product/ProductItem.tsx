import { FaBolt, FaStar, FaRegStar } from 'react-icons/fa6';
import Link from 'next/link';

export const ProductItem = () => {
    return (
        <div className="product-item bg-white rounded-[10px] overflow-hidden shadow-[0px_2.38px_2.38px_0px_#00000040]">
            <div className="inner-image aspect-[271/237] rounded-[10px] overflow-hidden mb-[-14px]">
                <Link href="#">
                    <img
                        src="/assets/images/product-1.jpg"
                        alt=""
                        className="w-full h-full object-cover"
                    />
                </Link>
            </div>
            <div className="inner-discount bg-[#DA0808] rounded-r-[32px] inline-flex items-center p-[3px_5px] gap-[6px] font-semibold text-[11px] text-[#F9F9FA] uppercase relative z-10">
                <FaBolt className="text-[#FFCB06] text-[11px]" /> Giảm -30%
            </div>
            <div className="inner-content p-[10px] sm:p-[16px]">
                <h3 className="inner-title font-bold text-[14px] leading-[125%] mt-0 mb-[6px] h-[35px] overflow-hidden line-clamp-2">
                    <Link
                        href="#"
                        className="text-[#25282B] hover:text-[var(--color-primary)]"
                    >
                        Hà Nội – Lào Cai – SaPa 4N3Đ Lorem ipsum dolor sit amet consectetur
                        adipisicing elit. Nobis unde totam minus repellat tenetur. Aut ea
                        quasi veniam modi vel consectetur hic fugit eaque, neque suscipit
                        in, recusandae dolor qui!
                    </Link>
                </h3>
                <div className="inner-prices flex flex-wrap gap-[5px_11.5px] mb-[6px]">
                    <div className="inner-price-old font-semibold text-[15px] text-[#52575C] line-through">
                        13.650.000<span className="inner-unit underline">đ</span>
                    </div>
                    <div className="inner-price-new font-bold text-[15px] text-[#DA0808]">
                        2.590.000<span className="inner-unit underline">đ</span>
                    </div>
                </div>
                <div className="inner-desc mb-[10px] flex flex-col gap-[4px]">
                    <div className="inner-desc-item font-normal text-[10px] sm:text-[11px] text-[#8C8C8C]">
                        Mã Tour: <b className="font-bold">123456789</b>
                    </div>
                    <div className="inner-desc-item font-normal text-[10px] sm:text-[11px] text-[#8C8C8C]">
                        Ngày Khởi Hành: <b className="font-bold">22/07/2024</b>
                    </div>
                    <div className="inner-desc-item font-normal text-[10px] sm:text-[11px] text-[#8C8C8C]">
                        Thời Gian: <b className="font-bold">10 Ngày 9 Đêm</b>
                    </div>
                </div>
                <div className="inner-meta flex items-center justify-between flex-wrap gap-[10px]">
                    <div className="inner-rating inline-flex items-center gap-[4px]">
                        <div className="inner-stars text-[14px] text-[#FFD600] flex">
                            <FaStar />
                            <FaStar />
                            <FaStar />
                            <FaStar />
                            <FaRegStar />
                        </div>
                        <div className="inner-number font-normal text-[11px] text-[#8C8C8C]">
                            (5)
                        </div>
                    </div>
                    <div className="inner-stock flex flex-col items-start sm:flex-row sm:items-center gap-[6px]">
                        <div className="inner-label text-[#25282B] font-bold text-[11px]">
                            Số chỗ còn:
                        </div>
                        <div className="inner-number w-[32px] h-[32px] rounded-[6px] bg-[#DA0808] inline-flex items-center justify-center font-bold text-[14px] text-white">
                            10
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
