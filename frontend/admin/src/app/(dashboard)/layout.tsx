"use client";
import { DashboardShell } from "@/components/layout/DashboardShell";
import { Toaster } from "react-hot-toast";

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <Toaster position="top-right" reverseOrder={false} />
      <DashboardShell>{children}</DashboardShell>
    </>
  );
}
