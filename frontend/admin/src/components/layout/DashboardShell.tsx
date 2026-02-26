"use client";
import { useState } from "react";
import { Header } from "@/components/header/Header";
import { Sider } from "@/components/sider/Sider";
import { ErrorBoundary } from "@/components/common/ErrorBoundary";

/**
 * Shared dashboard layout shell.
 * Used by both the dashboard layout and the not-found page
 * to avoid duplicating Header + Sider + main wrapper.
 */
export const DashboardShell = ({ children }: { children: React.ReactNode }) => {
  const [activeSider, setActiveSider] = useState(false);

  return (
    <>
      <Header activeSider={activeSider} setActiveSider={setActiveSider} />
      <Sider activeSider={activeSider} setActiveSider={setActiveSider} />
      <div
        className="absolute top-[70px] lg:left-[240px] left-0 lg:w-[calc(100%-240px)] w-full min-h-[calc(100vh-70px)]
        bg-[#F4F5F9] py-[30px] sm:px-[30px] px-[15px]"
      >
        <ErrorBoundary>{children}</ErrorBoundary>
      </div>
    </>
  );
};
