"use client";
import { useEffect, useRef } from "react";
import Chart from "chart.js/auto";

export const RevenueChart = () => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const chartRef = useRef<Chart | null>(null);

  useEffect(() => {
    if (!canvasRef.current) return;

    // Destroy previous chart instance to avoid canvas reuse errors
    if (chartRef.current) {
      chartRef.current.destroy();
    }

    chartRef.current = new Chart(canvasRef.current, {
      type: "line",
      data: {
        labels: ["01", "02", "03", "04", "05"],
        datasets: [
          {
            label: "Tháng 05/2025",
            data: [1000000, 2000000, 3000000, 4000000, 5000000],
            borderColor: "#36A1EA",
            borderWidth: 1.5,
          },
          {
            label: "Tháng 04/2025",
            data: [1000000, 2000000, 2000000, 1000000, 6000000],
            borderColor: "#FE6383",
            borderWidth: 1.5,
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            position: "bottom",
          },
        },
        scales: {
          x: {
            display: true,
            title: {
              display: true,
              text: "Ngày",
            },
          },
          y: {
            display: true,
            title: {
              display: true,
              text: "Doanh thu (VNĐ)",
            },
          },
        },
        maintainAspectRatio: false,
      },
    });

    return () => {
      if (chartRef.current) {
        chartRef.current.destroy();
        chartRef.current = null;
      }
    };
  }, []);

  return (
    <div className="mt-[30px] bg-white shadow-[6px_6px_54px_0px_#0000000D] rounded-[14px] p-[32px]">
      <div className="flex items-center justify-between flex-wrap gap-[10px] mb-[32px]">
        <h2 className="m-0 font-[700] text-[24px] text-[var(--color-text)]">
          Biểu đồ doanh thu
        </h2>
        <div className="">
          <input
            type="month"
            className="h-[28px] border border-[#D5D5D5] rounded-[4px] font-[600] text-[12px] px-[16px]"
          />
        </div>
      </div>
      <div className="h-[350px]">
        <canvas ref={canvasRef}></canvas>
      </div>
    </div>
  );
};
