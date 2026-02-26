"use client";
import Link from "next/link";
import { IconType } from "react-icons";
import { useRouter, usePathname } from "next/navigation";
import { useState } from "react";
import { FaChevronDown } from "react-icons/fa6";
import { authApi } from "@/services/auth/auth.service";
import { ConfirmModal } from "@/components/common/ConfirmModal";

export type MenuItem = {
  name: string;
  link: string;
  icon: IconType;
  isLogout?: boolean;
};

export type MenuGroup = {
  name: string;
  icon: IconType;
  children: MenuItem[];
};

export type SiderItem = MenuItem | MenuGroup;

const isMenuGroup = (item: SiderItem): item is MenuGroup => {
  return "children" in item;
};

export const SiderMenu = (props: {
  menuList: SiderItem[];
  activeMenu: string;
  setActiveMenu: (value: string) => void;
}) => {
  const { menuList, activeMenu, setActiveMenu } = props;
  const router = useRouter();
  const pathname = usePathname();

  // Tự động mở group nếu child đang active
  const getInitialOpenGroups = (): string[] => {
    return menuList
      .filter(isMenuGroup)
      .filter((group) => group.children.some((child) => pathname.startsWith(child.link)))
      .map((group) => group.name);
  };

  const [openGroups, setOpenGroups] = useState<string[]>(getInitialOpenGroups);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const toggleGroup = (groupName: string) => {
    setOpenGroups((prev) =>
      prev.includes(groupName) ? prev.filter((n) => n !== groupName) : [...prev, groupName],
    );
  };

  const handleClick = (e: React.MouseEvent, item: MenuItem) => {
    if (item.isLogout) {
      e.preventDefault();
      setShowLogoutModal(true);
      return;
    }
    setActiveMenu(item.name);
  };

  const handleLogoutConfirm = async () => {
    try {
      await authApi.logout();
      router.push("/login");
    } catch {
      alert("Đăng xuất thất bại");
    } finally {
      setShowLogoutModal(false);
    }
  };

  const isChildActive = (group: MenuGroup): boolean => {
    return group.children.some((child) => activeMenu === child.name);
  };

  const renderMenuItem = (item: MenuItem) => (
    <li key={item.name}>
      <Link
        href={item.link}
        onClick={(e) => handleClick(e, item)}
        className={`flex items-center gap-[10px] py-[11px] px-[16px] ml-[24px] mr-[10px] rounded-[6px] font-[600] text-[14px] relative cursor-pointer
          ${
            activeMenu === item.name
              ? "bg-[var(--color-primary)] before:absolute before:left-[-24px] before:top-0 before:h-full before:w-[4.5px] before:rounded-[0_4px_4px_0] before:bg-[var(--color-primary)] before:content-['']"
              : ""
          } 
          ${
            activeMenu === item.name
              ? "text-white"
              : item.isLogout
                ? "text-[#F93C65]"
                : "text-[var(--color-text)]"
          }`}
      >
        <item.icon className="text-[16px]" /> {item.name}
      </Link>
    </li>
  );

  const renderMenuGroup = (group: MenuGroup) => {
    const isOpen = openGroups.includes(group.name);
    const hasActiveChild = isChildActive(group);

    return (
      <li key={group.name}>
        {/* Group Header */}
        <button
          type="button"
          onClick={() => toggleGroup(group.name)}
          className={`flex items-center gap-[10px] py-[11px] px-[16px] ml-[24px] mr-[10px] rounded-[6px] font-[600] text-[14px] w-[calc(100%-34px)] cursor-pointer border-0 bg-transparent relative
            ${
              hasActiveChild && !isOpen
                ? "bg-[rgba(72,128,255,0.08)] text-[var(--color-primary)]"
                : "text-[var(--color-text)]"
            }
            hover:bg-[#F4F5F9] transition-colors duration-200`}
        >
          <group.icon className="text-[16px]" />
          <span className="flex-1 text-left">{group.name}</span>
          <FaChevronDown
            className={`text-[12px] transition-transform duration-200 ${isOpen ? "rotate-180" : ""}`}
          />
        </button>

        {/* Group Children */}
        <div
          className={`overflow-hidden transition-all duration-200 ease-in-out ${isOpen ? "max-h-[500px] opacity-100" : "max-h-0 opacity-0"}`}
        >
          <ul className="list-none p-0 m-0">
            {group.children.map((child) => (
              <li key={child.name}>
                <Link
                  href={child.link}
                  onClick={(e) => handleClick(e, child)}
                  className={`flex items-center gap-[10px] py-[10px] pl-[28px] pr-[16px] ml-[24px] mr-[10px] rounded-[6px] font-[600] text-[13px] relative cursor-pointer
                    ${
                      activeMenu === child.name
                        ? "bg-[var(--color-primary)] text-white before:absolute before:left-[-24px] before:top-0 before:h-full before:w-[4.5px] before:rounded-[0_4px_4px_0] before:bg-[var(--color-primary)] before:content-['']"
                        : "text-[var(--color-text)] opacity-80 hover:opacity-100 hover:bg-[#F4F5F9]"
                    }
                    transition-colors duration-150`}
                >
                  <child.icon className="text-[14px]" /> {child.name}
                </Link>
              </li>
            ))}
          </ul>
        </div>
      </li>
    );
  };

  return (
    <>
      <ul className="list-none p-0 my-[11px] mx-0">
        {menuList.map((item) => (isMenuGroup(item) ? renderMenuGroup(item) : renderMenuItem(item)))}
      </ul>

      <ConfirmModal
        isOpen={showLogoutModal}
        onClose={() => setShowLogoutModal(false)}
        onConfirm={handleLogoutConfirm}
        title="Xác nhận đăng xuất"
        message="Bạn có chắc chắn muốn đăng xuất khỏi hệ thống không?"
      />
    </>
  );
};
