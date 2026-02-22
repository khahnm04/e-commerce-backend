import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Trang chủ",
  description: "Hệ thống kinh doanh thiết bị công nghệ",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="vi" data-yd-content-ready="true">
      <body suppressHydrationWarning>
        {children}
      </body>
    </html>
  );
}
