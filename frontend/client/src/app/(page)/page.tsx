import { HeroSearch } from "@/components/home/HeroSearch";
import { FlashSaleCountdown } from "@/components/home/FlashSaleCountdown";
import { PromotionBanner } from "@/components/home/PromotionBanner";
import { FeaturedProducts } from "@/components/home/FeaturedProducts";
import { FullWidthBanner } from "@/components/home/FullWidthBanner";
import { LatestNews } from "@/components/home/LatestNews";

export default function HomePage() {
  return (
    <>
      <HeroSearch />

      <FlashSaleCountdown />

      <PromotionBanner />

      <FeaturedProducts />

      <FullWidthBanner />

      <LatestNews />
    </>
  )
}
