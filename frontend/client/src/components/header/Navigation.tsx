import Link from "next/link";
import { FaCaretDown, FaCaretUp } from "react-icons/fa6";
import { IoIosCloseCircleOutline } from "react-icons/io";
import { menuData } from "./menuData";

interface NavigationProps {
  activeMenuMobile: boolean;
  activeItemMobile: number[];
  onClickOpenMenuMobile: () => void;
  onClickOpenItemMobile: (index: number) => void;
}

export const Navigation = ({
  activeMenuMobile,
  activeItemMobile,
  onClickOpenMenuMobile,
  onClickOpenItemMobile,
}: NavigationProps) => {
  return (
    <nav className={`inner-menu lg:block ${activeMenuMobile ? "block fixed top-[0px] left-[0px] w-[100%] h-[100%] z-[999]" : "hidden"}`}>
      <ul className={`
            inline-flex list-none p-[0px] m-[0px] xl:gap-[40px] lg:gap-[24px]
            ${activeMenuMobile ? "w-[280px] h-[100%] bg-[#FFFFFF] flex-col relative z-[1] gap-[0px] rounded-r-[5px]" : ""}
          `}
      >
        <div className={`${activeMenuMobile ? "p-[10px] mt-[5px] flex justify-between items-center" : "hidden"}`}>
          <span className="text-[20px] font-[600] text-[var(--color-primary)]">
            Menu
          </span>
          <IoIosCloseCircleOutline
            className="text-[25px] hover:text-[var(--color-primary)] cursor-pointer"
            onClick={onClickOpenMenuMobile}
          />
        </div>
        {menuData.map((item, index) => (
          <li
            key={index}
            className={`
              relative group inline-flex items-center gap-[6px]
              ${activeMenuMobile ? "m-[0px] flex flex-wrap border-b border-b-[#ddd]" : ""}
            `}
            // Note: The original generic onClick on li might trigger when clicking submenu items if not prevented,
            // but in the original code it was on the li. 
            // However, looking at original code: onClick={() => handleClickOpenItemMobile(index)}
            // This is applied to the li.
            onClick={() => onClickOpenItemMobile(index)}
          >
            <Link
              href={item.href}
              className={`
                font-[500] text-[16px] text-[#25282B] capitalize hover:text-[var(--color-primary)] 
                ${item.active ? "text-[var(--color-primary)]" : ""}
                ${activeMenuMobile ? "flex-1 py-[10px] px-[10px] pl-[20px]" : ""}
              `}
            >
              {item.label}
            </Link>

            {/* Có submenu thì render tiếp */}
            {item.children && (
              <>
                <span className={`${activeMenuMobile ? "w-[40px] inline-flex items-center justify-center h-[44px]" : ""}`}>
                  {activeItemMobile.includes(index) ? <FaCaretUp /> : <FaCaretDown />}
                </span>
                <ul className={`
                      absolute top-[100%] left-[0px] list-none lg:py-[8px] py-[0px] lg:pb-[0px] pb-[20px] lg:pl-[0px] pl-[10px] px-[0px] m-[0px] bg-[#FFFFFF] rounded-b-[10px] w-[280px] z-[999] hidden lg:group-hover:block
                      ${activeMenuMobile && activeItemMobile.includes(index) ? "!block static w-full p-[0px]" : ""}
                    `}
                >
                  {item.children.map((subItem, subIndex) => (
                    <li key={subIndex}>
                      <Link
                        href="#"
                        className="block py-[8px] px-[22px] font-[400] text-[16px] text-[var(--color-primary)] hover:bg-[var(--color-primary)] hover:text-[#FFFFFF]"
                      >
                        {subItem}
                      </Link>
                    </li>
                  ))}
                </ul>
              </>
            )}
          </li>
        ))}
      </ul>
      <div
        className={`${activeMenuMobile ? "inner-overlay absolute top-[0px] left-[0px] w-[100%] h-[100%] bg-[#00000088]" : ""}`}
        onClick={onClickOpenMenuMobile}
      >
      </div>
    </nav>
  );
};
