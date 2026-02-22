import Link from 'next/link';
import { FaFacebook, FaInstagram, FaXTwitter, FaYoutube } from 'react-icons/fa6';

export const Footer = () => {
    return (
        <footer className="footer bg-[#ECECEC] rounded-t-[25px] sm:rounded-t-[40px] py-[40px] sm:py-[60px] pb-[40px]">
            <div className="container mx-auto px-4">
                {/* Top Section */}
                <div className="inner-top bg-[var(--color-primary)] rounded-[16px] p-[20px] sm:p-[32px] flex flex-col lg:flex-row items-center gap-[20px] sm:gap-[24px] mb-[30px] sm:mb-[40px]">
                    <h2 className="inner-title m-0 w-full lg:w-[389px] font-bold text-[18px] sm:text-[20px] text-[#FDFDFD] capitalize text-center lg:text-left">
                        Đăng ký ngay để không bỏ lỡ các chương trình của chúng tôi
                    </h2>
                    <form action="" className="inner-form bg-white rounded-[14px] p-[12px] flex-1 flex flex-col sm:flex-row gap-[12px] relative w-full" id="email-form">
                        <input
                            id="email-input"
                            name="email"
                            type="email"
                            placeholder="Nhập email của bạn..."
                            className="w-full sm:flex-1 outline-none border border-[#99A2A5] rounded-[8px] h-[48px] px-[28px] font-medium text-[14px] placeholder-[#99A2A5]"
                        />
                        <button className="bg-[var(--color-primary)] h-[48px] border-0 rounded-[8px] px-[23.5px] font-medium text-[16px] text-[#FDFDFD] cursor-pointer w-full sm:w-auto transition-colors hover:brightness-110">
                            Đăng Ký Ngay
                        </button>
                    </form>
                </div>

                {/* Middle Section */}
                <div className="inner-middle flex flex-col lg:flex-row items-center justify-between gap-[20px] pb-[30px] sm:pb-[40px] border-b border-[#CCD1D2] mb-[30px] sm:mb-[40px]">
                    <nav className="inner-links">
                        <ul className="m-0 p-0 list-none inline-flex flex-wrap justify-center gap-[10px_25px] sm:gap-[10px_40px]">
                            <li>
                                <Link href="#" className="font-medium text-[14px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    Trang Chủ
                                </Link>
                            </li>
                            <li>
                                <Link href="#" className="font-medium text-[14px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    Tour Trong Nước
                                </Link>
                            </li>
                            <li>
                                <Link href="#" className="font-medium text-[14px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    Tour Nước Ngoài
                                </Link>
                            </li>
                            <li>
                                <Link href="#" className="font-medium text-[14px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    Tin Tức
                                </Link>
                            </li>
                            <li>
                                <Link href="#" className="font-medium text-[14px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    Liên Hệ
                                </Link>
                            </li>
                        </ul>
                    </nav>
                    <nav className="inner-socials">
                        <ul className="m-0 p-0 list-none inline-flex gap-[10px_25px] sm:gap-[10px_40px]">
                            <li>
                                <Link href="#" target="_blank" className="text-[24px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    <FaFacebook />
                                </Link>
                            </li>
                            <li>
                                <Link href="#" target="_blank" className="text-[24px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    <FaXTwitter />
                                </Link>
                            </li>
                            <li>
                                <Link href="#" target="_blank" className="text-[24px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    <FaInstagram />
                                </Link>
                            </li>
                            <li>
                                <Link href="#" target="_blank" className="text-[24px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors">
                                    <FaYoutube />
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </div>

                {/* Bottom Section */}
                <div className="inner-bottom flex flex-wrap lg:flex-nowrap items-center">
                    <div className="inner-copyright w-full lg:w-[calc(50%-112px)] order-3 lg:order-1 text-center lg:text-left font-medium text-[13px] text-[#667479]">
                        © 2024 28Tech. All rights reserved.
                    </div>
                    <div className="inner-logo w-full lg:w-[224px] order-1 lg:order-2 mb-[20px] lg:mb-0">
                        <Link href="/">
                            <img src="/assets/images/logo.png" alt="28.TRAVEL" className="w-[224px] lg:w-full h-auto mx-auto block" />
                        </Link>
                    </div>
                    <nav className="inner-links w-full lg:w-[calc(50%-112px)] order-2 lg:order-3">
                        <ul className="list-none p-0 m-0 flex justify-center lg:justify-end gap-[10px_25px] sm:gap-[10px_30px] mb-[20px] lg:mb-0">
                            <li>
                                <Link href="#" className="font-medium text-[13px] text-[#667479] hover:text-[var(--color-primary)] transition-colors">
                                    Điều khoản dịch vụ
                                </Link>
                            </li>
                            <li>
                                <Link href="#" className="font-medium text-[13px] text-[#667479] hover:text-[var(--color-primary)] transition-colors">
                                    Chính sách bảo mật
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </footer>
    );
};
