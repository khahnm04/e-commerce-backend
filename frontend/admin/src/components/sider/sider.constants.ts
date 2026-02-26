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
  FaShieldHalved,
} from "react-icons/fa6";
import { TiMessages } from "react-icons/ti";
import { SiderItem } from "./SiderMenu";

export const MAIN_MENU: SiderItem[] = [
  {
    name: "Tổng quan",
    link: "/",
    icon: FaGaugeHigh,
  },
  {
    name: "Sản phẩm",
    icon: FaCartShopping,
    children: [
      {
        name: "Quản lý nhãn hàng",
        link: "/brands",
        icon: FaIndustry,
      },
      {
        name: "Quản lý danh mục",
        link: "/categories",
        icon: FaListUl,
      },
      {
        name: "Quản lý sản phẩm",
        link: "/products",
        icon: FaBoxOpen,
      },
      {
        name: "Biến thể sản phẩm",
        link: "/product-variants",
        icon: FaBoxesStacked,
      },
      {
        name: "Quản lý thuộc tính",
        link: "/attributes",
        icon: FaTag,
      },
      {
        name: "Quản lý biến thể",
        link: "/variants",
        icon: FaSliders,
      },
      {
        name: "Giá trị biến thể",
        link: "/variant-values",
        icon: FaPalette,
      },
    ],
  },
  {
    name: "Kinh doanh",
    icon: FaListCheck,
    children: [
      {
        name: "Quản lý đơn hàng",
        link: "/orders",
        icon: FaListCheck,
      },
      {
        name: "Mã giảm giá",
        link: "/coupons",
        icon: FaGift,
      },
    ],
  },
  {
    name: "Kho vận",
    icon: FaWarehouse,
    children: [
      {
        name: "Thông tin kho",
        link: "/inventories",
        icon: FaWarehouse,
      },
    ],
  },
  {
    name: "Nội dung",
    icon: FaNewspaper,
    children: [
      {
        name: "Banner / Marketing",
        link: "/banners",
        icon: FaBullhorn,
      },
      {
        name: "Tin tức / Bài viết",
        link: "/news",
        icon: FaNewspaper,
      },
    ],
  },
  {
    name: "Chi nhánh",
    link: "/branches",
    icon: FaStore,
  },
  {
    name: "Khách hàng",
    link: "/customers",
    icon: FaUsers,
  },
  {
    name: "Nhân viên",
    link: "/employees",
    icon: FaUserGear,
  },
  {
    name: "Nhóm quyền",
    link: "/roles",
    icon: FaShieldHalved,
  },
  {
    name: "Thông báo",
    link: "/notifications",
    icon: FaBell,
  },
  {
    name: "Tin nhắn",
    link: "/messages",
    icon: TiMessages,
  },
];

export const BOTTOM_MENU: SiderItem[] = [
  {
    name: "Cài đặt chung",
    link: "/settings",
    icon: FaGear,
  },
  {
    name: "Thông tin cá nhân",
    link: "/my-profile",
    icon: FaUserGear,
  },
  {
    name: "Đăng xuất",
    link: "/logout",
    icon: FaPowerOff,
    isLogout: true,
  },
];
