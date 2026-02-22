import Link from "next/link";

export const Cart = () => {
  return (
    <div className="inner-cart">
      <Link
        href="#"
        className="relative inline-flex items-center"
      >
        <img
          src="/assets/images/icon-cart.svg"
          alt="Cart"
          className="h-[26px] w-auto"
        />
        <span className="absolute -top-1 -right-2 bg-[#FF3904] rounded-full text-[10px] text-[#FFFFFF] flex items-center justify-center w-4 h-4 font-bold">
          1
        </span>
      </Link>
    </div>
  );
};
