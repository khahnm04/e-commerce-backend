import { FaAnglesLeft, FaAnglesRight } from "react-icons/fa6";

export const BoxPagination = () => {
  const buttonClass =
    "w-[40px] h-[40px] border border-[#4502C7] border-r-0 bg-transparent font-normal text-[14px] text-[var(--color-primary)] cursor-pointer flex items-center justify-center transition-colors hover:bg-[var(--color-primary)] hover:text-[#F8F6EB]";
  const activeClass = "!bg-[var(--color-primary)] !text-[#F8F6EB]";

  return (
    <div className="box-pagination flex justify-end mt-[30px]">
      <button className={`${buttonClass} rounded-l-[5px]`}>
        <FaAnglesLeft className="text-[10px]" />
      </button>
      <button className={`${buttonClass} ${activeClass}`}>
        1
      </button>
      <button className={buttonClass}>
        2
      </button>
      <button className={buttonClass}>
        3
      </button>
      <button className={buttonClass}>
        4
      </button>
      <button className={`${buttonClass} !border-r rounded-r-[5px]`}>
        <FaAnglesRight className="text-[10px]" />
      </button>
    </div>
  );
};
