"use client"
import { useState, useEffect } from "react";
import { Logo } from "./Logo";
import { Cart } from "./Cart";
import { Wishlist } from "./Wishlist";
import { Account } from "./Account";
import { MobileMenuToggle } from "./MobileMenuToggle";
import { Navigation } from "./Navigation";

export const Header = () => {

  const [activeMenuMobile, setActiveMenuMobile] = useState(false);
  const [activeItemMobile, setActiveItemMobile] = useState<number[]>([]);

  const handleClickOpenMenuMobile = () => {
    if (activeMenuMobile) {
      setActiveItemMobile([]);
    }
    setActiveMenuMobile(!activeMenuMobile);
  }

  const handleClickOpenItemMobile = (index: number) => {
    if (activeMenuMobile) {
      if (activeItemMobile.includes(index)) {
        setActiveItemMobile(activeItemMobile.filter(item => item !== index));
      } else {
        setActiveItemMobile([...activeItemMobile, index]);
      }
    }
  }

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth >= 992 && activeMenuMobile) {
        setActiveMenuMobile(false);
        setActiveItemMobile([]);
      }
    }

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    }
  }, [activeMenuMobile]);

  return (
    <>
      <header className="header bg-[#FFFFFF] sm:py-[12px] py-[15px] sticky top-0 left-0 z-[999]">
        <div className="container">
          <div className="inner-wrap flex justify-between items-center">

            <MobileMenuToggle onClick={handleClickOpenMenuMobile} />

            <Logo />

            <Navigation
              activeMenuMobile={activeMenuMobile}
              activeItemMobile={activeItemMobile}
              onClickOpenMenuMobile={handleClickOpenMenuMobile}
              onClickOpenItemMobile={handleClickOpenItemMobile}
            />

            <div className="flex items-center gap-[20px]">
              <Wishlist />
              <Cart />
              <Account />
            </div>

          </div>
        </div>
      </header>
    </>
  )
}

