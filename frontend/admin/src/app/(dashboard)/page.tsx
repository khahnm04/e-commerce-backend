"use client";
import { DashboardStats } from "../../components/dashboard/DashboardStats";
import { RevenueChart } from "../../components/dashboard/RevenueChart";
import { RecentOrdersTable } from "../../components/dashboard/RecentOrdersTable";

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
