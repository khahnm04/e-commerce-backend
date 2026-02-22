import { FaBars } from "react-icons/fa6";

interface MobileMenuToggleProps {
  onClick: () => void;
}

export const MobileMenuToggle = ({ onClick }: MobileMenuToggleProps) => {
  return (
    <button
      className="inner-menu-mobile lg:hidden inline-block bg-[#FFFFFF] p-[0px] border-[0px] text-[22px] cursor-pointer text-[#25282B]"
      onClick={onClick}
    >
      <FaBars className="" />
    </button>
  );
};
