"use client"
import { useState } from "react";
import {
  FaBoxOpen,
  FaBoxesStacked,
  FaBullhorn,
  FaCartShopping,
  FaGift,
  FaGaugeHigh,
  FaGear,
  FaIndustry,
  FaListCheck,
  FaListUl,
  FaNewspaper,
  FaPalette,
  FaPowerOff,
  FaSliders,
  FaStore,
  FaTag,
  FaUserGear,
  FaUsers,
  FaWarehouse,
  FaBell,
  FaShieldHalved
} from "react-icons/fa6";
import { SiderMenu, SiderItem } from "./SiderMenu";
import { TiMessages } from "react-icons/ti";

export const Sider = (props: {
  activeSider: boolean;
  setActiveSider: (value: boolean) => void;
}) => {

  const mainMenu: SiderItem[] = [
    {
      name: "Tổng quan",
      link: "/",
      icon: FaGaugeHigh
    },
    {
      name: "Sản phẩm",
      icon: FaCartShopping,
      children: [
        {
          name: "Quản lý nhãn hàng",
          link: "/brands",
          icon: FaIndustry
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
          name: "Biến thể sản phẩm",
          link: "/product-variants",
          icon: FaBoxesStacked
        },
        {
          name: "Quản lý thuộc tính",
          link: "/attributes",
          icon: FaTag
        },
        {
          name: "Quản lý biến thể",
          link: "/variants",
          icon: FaSliders
        },
        {
          name: "Giá trị biến thể",
          link: "/variant-values",
          icon: FaPalette
        },
      ]
    },
    {
      name: "Kinh doanh",
      icon: FaListCheck,
      children: [
        {
          name: "Quản lý đơn hàng",
          link: "/orders",
          icon: FaListCheck
        },
        {
          name: "Mã giảm giá",
          link: "/coupons",
          icon: FaGift
        },
      ]
    },
    {
      name: "Kho vận",
      icon: FaWarehouse,
      children: [
        {
          name: "Thông tin kho",
          link: "/inventories",
          icon: FaWarehouse
        },
      ]
    },
    {
      name: "Nội dung",
      icon: FaNewspaper,
      children: [
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
      ]
    },
    {
      name: "Chi nhánh",
      link: "/branches",
      icon: FaStore
    },
    {
      name: "Khách hàng",
      link: "/customers",
      icon: FaUsers
    },
    {
      name: "Nhân viên",
      link: "/employees",
      icon: FaUserGear
    },
    {
      name: "Thông báo",
      link: "/notifications",
      icon: FaBell
    },
    {
      name: "Tin nhắn",
      link: "/messages",
      icon: TiMessages
    },
    {
      name: "Nhóm quyền",
      link: "/roles",
      icon: FaShieldHalved
    },
  ];

  const bottomMenu: SiderItem[] = [
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
  ];

  const { activeSider, setActiveSider } = props;
  const [activeMenu, setActiveMenu] = useState("Tổng quan");

  return (
    <>
      <nav className={`bg-[#FFFFFF] border-r border-[#E0E0E0] lg:w-[240px] w-[280px] lg:h-[calc(100vh-70px)] h-full fixed 
        lg:top-[70px] top-0 left-0 overflow-y-auto [&::-webkit-scrollbar]:w-[3px] [&::-webkit-scrollbar-thumb]:bg-[#ddd] z-[999] 
        lg:block ${activeSider ? 'block' : 'hidden'}`}>
        <SiderMenu
          menuList={mainMenu}
          activeMenu={activeMenu}
          setActiveMenu={setActiveMenu}
        />
        <hr className="border border-[#E0E0E0] my-[16px] mx-0" />
        <SiderMenu
          menuList={bottomMenu}
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
