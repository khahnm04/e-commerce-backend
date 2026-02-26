"use client";
import { useState, useEffect, useCallback, useMemo } from "react";
import { usePathname } from "next/navigation";
import { MAIN_MENU, BOTTOM_MENU } from "./sider.constants";
import { SiderMenu, SiderItem } from "./SiderMenu";

export const Sider = (props: {
  activeSider: boolean;
  setActiveSider: (value: boolean) => void;
}) => {
  const { activeSider, setActiveSider } = props;
  const pathname = usePathname();

  // Collect all menu items (flat) from both menus
  const allMenuItems = useMemo(() => {
    const items: { name: string; link: string }[] = [];
    const collect = (list: SiderItem[]) => {
      for (const item of list) {
        if ("children" in item) {
          for (const child of item.children) {
            items.push({ name: child.name, link: child.link });
          }
        } else {
          items.push({ name: item.name, link: item.link });
        }
      }
    };
    collect(MAIN_MENU);
    collect(BOTTOM_MENU);
    return items;
  }, []);

  // Find the active menu based on pathname (longest match wins)
  const getActiveMenuFromPath = useCallback(
    (path: string): string => {
      let bestMatch = "Tổng quan";
      let bestLength = 0;
      for (const item of allMenuItems) {
        if (item.link === "/" && path === "/") {
          return item.name;
        }
        if (item.link !== "/" && path.startsWith(item.link) && item.link.length > bestLength) {
          bestMatch = item.name;
          bestLength = item.link.length;
        }
      }
      return bestMatch;
    },
    [allMenuItems],
  );

  const [activeMenu, setActiveMenu] = useState(() => getActiveMenuFromPath(pathname));

  // Sync active menu when pathname changes (e.g. navigating via links in the page)
  useEffect(() => {
    setActiveMenu(getActiveMenuFromPath(pathname));
  }, [pathname, getActiveMenuFromPath]);

  return (
    <>
      <nav
        className={`bg-[#FFFFFF] border-r border-[#E0E0E0] lg:w-[240px] w-[280px] lg:h-[calc(100vh-70px)] h-full fixed 
        lg:top-[70px] top-0 left-0 overflow-y-auto [&::-webkit-scrollbar]:w-[3px] [&::-webkit-scrollbar-thumb]:bg-[#ddd] z-[999] 
        lg:block ${activeSider ? "block" : "hidden"}`}
      >
        <SiderMenu menuList={MAIN_MENU} activeMenu={activeMenu} setActiveMenu={setActiveMenu} />
        <hr className="border border-[#E0E0E0] my-[16px] mx-0" />
        <SiderMenu menuList={BOTTOM_MENU} activeMenu={activeMenu} setActiveMenu={setActiveMenu} />
      </nav>
      <div
        className={`${activeSider ? "lg:hidden block" : "hidden"} fixed top-0 left-0 w-full h-full bg-[#0000008d] z-[998]`}
        onClick={() => setActiveSider(false)}
      ></div>
    </>
  );
};
