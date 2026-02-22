import Link from "next/link";
import { FaRegHeart } from "react-icons/fa";

export const Wishlist = () => {
    return (
        <div className="inner-wishlist">
            <Link
                href="#"
                className="relative inline-flex items-center"
            >
                <FaRegHeart className="text-[26px] text-black" />
                <span className="absolute -top-1 -right-2 bg-[#FF3904] rounded-full text-[10px] text-[#FFFFFF] flex items-center justify-center w-4 h-4 font-bold">
                    0
                </span>
            </Link>
        </div>
    );
};
