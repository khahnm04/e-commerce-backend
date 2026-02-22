"use client"
import { useState } from "react";
import {
  FaBoxOpen,
  FaBoxesStacked,
  FaBullhorn,
  FaGift,
  FaGaugeHigh,
  FaGear,
  FaListCheck,
  FaListUl,
  FaNewspaper,
  FaPowerOff,
  FaStore,
  FaTag,
  FaUserGear,
  FaUsers,
  FaWarehouse,
  FaBell
} from "react-icons/fa6";
import { SiderMenu } from "./SiderMenu";

export const Sider = (props: {
  activeSider: boolean;
  setActiveSider: (value: boolean) => void;
}) => {
  const menuList1 = [
    {
      name: "Tổng quan",
      link: "/",
      icon: FaGaugeHigh
    },
    {
      name: "Quản lý danh mục",
      link: "/categories",
      icon: FaListUl
    },
    {
      name: "Quản lý sản phẩm",
      link: "/products",
      icon: FaBoxOpen
    },
    {
      name: "Thuộc tính sản phẩm",
      link: "/product-attributes",
      icon: FaTag
    },
    {
      name: "Biến thể sản phẩm",
      link: "/product-variants",
      icon: FaBoxesStacked
    },
    {
      name: "Quản lý đơn hàng",
      link: "/orders",
      icon: FaListCheck
    },
    {
      name: "Thông tin kho",
      link: "/inventories",
      icon: FaWarehouse
    },
    {
      name: "Quản lý chi nhánh",
      link: "/branches",
      icon: FaStore
    },
    {
      name: "Banner / Marketing",
      link: "/banners",
      icon: FaBullhorn
    },
    {
      name: "Tin tức / Bài viết",
      link: "/news",
      icon: FaNewspaper
    },
    {
      name: "Mã giảm giá",
      link: "/coupons",
      icon: FaGift
    },
    {
      name: "Quản lý người dùng",
      link: "/users",
      icon: FaUsers
    },
    {
      name: "Thông báo",
      link: "/notifications",
      icon: FaBell
    },
  ]

  const menuList2 = [
    {
      name: "Cài đặt chung",
      link: "/settings",
      icon: FaGear
    },
    {
      name: "Thông tin cá nhân",
      link: "/my-profile",
      icon: FaUserGear
    },
    {
      name: "Đăng xuất",
      link: "/logout",
      icon: FaPowerOff,
      isLogout: true
    }
  ]

  const { activeSider, setActiveSider } = props;
  const [activeMenu, setActiveMenu] = useState(menuList1[0].name);

  return (
    <>
      <nav className={`bg-[#FFFFFF] border-r border-[#E0E0E0] lg:w-[240px] w-[280px] lg:h-[calc(100vh-70px)] h-full fixed 
        lg:top-[70px] top-0 left-0 overflow-y-auto [&::-webkit-scrollbar]:w-[3px] [&::-webkit-scrollbar-thumb]:bg-[#ddd] z-[999] 
        lg:block ${activeSider ? 'block' : 'hidden'}`}>
        <SiderMenu
          menuList={menuList1}
          activeMenu={activeMenu}
          setActiveMenu={setActiveMenu}
        />
        <hr className="border border-[#E0E0E0] my-[16px] mx-0" />
        <SiderMenu
          menuList={menuList2}
          activeMenu={activeMenu}
          setActiveMenu={setActiveMenu}
        />
      </nav>
      <div
        className={`${activeSider ? 'lg:hidden block' : 'hidden'} fixed top-0 left-0 w-full h-full bg-[#0000008d] z-[998]`}
        onClick={() => setActiveSider(false)}
      >
      </div>
    </>
  )
}
