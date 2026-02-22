import { Header } from "@/components/header/Header";
import { TopHeader } from "@/components/header/TopHeader";
import { Footer } from "@/components/footer/Footer";
import { BoxContact } from "@/components/common/BoxContact";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {

  return (
    <>
      <TopHeader />

      <Header />

      <div className="">
        {children}
      </div>

      <Footer />
      <BoxContact />
    </>
  );
}
