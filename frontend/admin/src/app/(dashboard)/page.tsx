"use client"
import { DashboardStats } from "./_components/DashboardStats";
import { RevenueChart } from "./_components/RevenueChart";
import { RecentOrdersTable } from "./_components/RecentOrdersTable";

export default function HomePage() {
  return (
    <>
      <h1 className="mt-0 mb-[30px] font-[700] text-[32px] text-[var(--color-text)]">
        Tổng quan
      </h1>

      <DashboardStats />
      <RevenueChart />
      <RecentOrdersTable />
    </>
  );
}
