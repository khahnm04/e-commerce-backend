'use client';

import { useState, useCallback } from 'react';
import Link from 'next/link';
import { FaClock, FaEye, FaTag } from 'react-icons/fa6';
import { BoxPagination } from '@/components/common/BoxPagination';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface NewsItem {
  id: number;
  slug: string;
  image: string;
  title: string;
  excerpt: string;
  date: string;
  views: number;
  category: string;
}

interface CategoryTab {
  key: string;
  label: string;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const CATEGORIES: CategoryTab[] = [
  { key: 'all', label: 'Tất Cả' },
  { key: 'tips', label: 'Mẹo Hay' },
  { key: 'review', label: 'Đánh Giá' },
  { key: 'promo', label: 'Khuyến Mại' },
  { key: 'event', label: 'Sự Kiện' },
];

const NEWS_ITEMS: NewsItem[] = [
  {
    id: 1,
    slug: 'homestay-co-tich-da-lat',
    image: '/assets/images/blog-1.jpg',
    title: 'Homestay "cổ tích" giữa Đà Lạt mộng mơ',
    excerpt: 'Khám phá những homestay đẹp như cổ tích giữa lòng Đà Lạt, nơi bạn có thể tận hưởng không gian yên bình và lãng mạn.',
    date: '23/05/2024',
    views: 1250,
    category: 'tips',
  },
  {
    id: 2,
    slug: 'top-5-diem-check-in-singapore',
    image: '/assets/images/blog-2.jpg',
    title: 'Top 5 điểm check in độc đáo tại Singapore',
    excerpt: 'Nếu bạn nghĩ du lịch Singapore rất nhàm chán vì chỉ mãi quanh quẩn trong những khối bê tông vô cảm thì bạn "chắc chưa?".',
    date: '20/05/2024',
    views: 2340,
    category: 'review',
  },
  {
    id: 3,
    slug: 'dac-san-da-nang-hoi-an',
    image: '/assets/images/blog-3.jpg',
    title: 'Đặc sản không thể bỏ qua khi đến với Đà Nẵng - Hội An',
    excerpt: 'Tổng hợp những món ăn đặc sản nổi tiếng nhất tại Đà Nẵng và Hội An mà bất kỳ du khách nào cũng nên thử.',
    date: '18/05/2024',
    views: 1890,
    category: 'tips',
  },
  {
    id: 4,
    slug: 'dao-phu-quy-binh-thuan',
    image: '/assets/images/blog-4.jpg',
    title: 'Đảo Phú Quý Bình Thuận hot trend năm 2024',
    excerpt: 'Đảo Phú Quý đang trở thành điểm đến hot nhất năm 2024 với biển xanh cát trắng và chi phí cực kỳ hợp lý.',
    date: '15/05/2024',
    views: 3210,
    category: 'review',
  },
  {
    id: 5,
    slug: 'trekking-cam-trai-da-lat',
    image: '/assets/images/blog-5.jpg',
    title: 'Trekking cắm trại tại Đà Lạt - Cảm nhận Đà Lạt qua từng bước chân',
    excerpt: 'Trải nghiệm trekking và cắm trại giữa rừng thông Đà Lạt, một hoạt động outdoor đang được giới trẻ yêu thích.',
    date: '12/05/2024',
    views: 1560,
    category: 'event',
  },
  {
    id: 6,
    slug: 'khuyen-mai-mua-he-2024',
    image: '/assets/images/banner-1.png',
    title: 'Khuyến mại mùa hè 2024 - Giảm đến 50%',
    excerpt: 'Chương trình khuyến mại lớn nhất mùa hè với hàng trăm tour giảm giá sốc lên đến 50%. Đặt ngay hôm nay!',
    date: '10/05/2024',
    views: 4520,
    category: 'promo',
  },
];

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const CategoryTabs = ({
  categories,
  activeKey,
  onChange,
}: {
  categories: CategoryTab[];
  activeKey: string;
  onChange: (key: string) => void;
}) => (
  <div className="flex flex-wrap gap-[8px] sm:gap-[12px] mb-[30px] sm:mb-[40px]">
    {categories.map(({ key, label }) => (
      <button
        key={key}
        type="button"
        onClick={() => onChange(key)}
        className={`px-[16px] sm:px-[24px] py-[8px] sm:py-[10px] rounded-[8px] font-medium text-[13px] sm:text-[14px] border-0 cursor-pointer transition-all ${activeKey === key
          ? 'bg-[var(--color-primary)] text-white shadow-[0px_4px_12px_rgba(69,2,199,0.3)]'
          : 'bg-white text-[#52575C] shadow-[0px_1px_4px_rgba(0,0,0,0.1)] hover:shadow-[0px_2px_8px_rgba(0,0,0,0.15)] hover:text-[var(--color-primary)]'
          }`}
      >
        {label}
      </button>
    ))}
  </div>
);

const FeaturedNewsCard = ({ item }: { item: NewsItem }) => (
  <Link
    href={`/news/${item.slug}`}
    className="block rounded-[16px] overflow-hidden shadow-[0px_2px_12px_rgba(0,0,0,0.1)] hover:shadow-[0px_4px_20px_rgba(0,0,0,0.15)] transition-all group"
  >
    <div className="relative aspect-[16/8] overflow-hidden">
      <img
        src={item.image}
        alt={item.title}
        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
      />
      <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/20 to-transparent" />
      <div className="absolute bottom-0 left-0 right-0 p-[20px] sm:p-[30px]">
        <span className="inline-block bg-[var(--color-primary)] text-white text-[11px] sm:text-[12px] font-medium px-[12px] py-[4px] rounded-[4px] mb-[10px]">
          {CATEGORIES.find((c) => c.key === item.category)?.label ?? item.category}
        </span>
        <h3 className="m-0 font-bold text-[20px] sm:text-[24px] text-white leading-[130%] mb-[8px]">
          {item.title}
        </h3>
        <p className="m-0 font-normal text-[13px] sm:text-[14px] text-white/80 line-clamp-2">
          {item.excerpt}
        </p>
        <div className="flex items-center gap-[16px] mt-[12px] text-white/70 text-[12px]">
          <span className="flex items-center gap-[5px]">
            <FaClock className="text-[11px]" /> {item.date}
          </span>
          <span className="flex items-center gap-[5px]">
            <FaEye className="text-[11px]" /> {item.views.toLocaleString('vi-VN')} lượt xem
          </span>
        </div>
      </div>
    </div>
  </Link>
);

const NewsCard = ({ item }: { item: NewsItem }) => (
  <Link
    href={`/news/${item.slug}`}
    className="block bg-white rounded-[12px] overflow-hidden shadow-[0px_1px_6px_rgba(0,0,0,0.08)] hover:shadow-[0px_4px_16px_rgba(0,0,0,0.12)] transition-all group"
  >
    <div className="aspect-[16/10] overflow-hidden">
      <img
        src={item.image}
        alt={item.title}
        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
      />
    </div>
    <div className="p-[14px] sm:p-[18px]">
      <div className="flex items-center gap-[8px] mb-[8px]">
        <span className="inline-flex items-center gap-[4px] text-[var(--color-primary)] text-[11px] sm:text-[12px] font-medium">
          <FaTag className="text-[10px]" />
          {CATEGORIES.find((c) => c.key === item.category)?.label ?? item.category}
        </span>
      </div>
      <h3 className="m-0 font-semibold text-[15px] sm:text-[16px] text-[#25282B] leading-[140%] mb-[8px] line-clamp-2 group-hover:text-[var(--color-primary)] transition-colors">
        {item.title}
      </h3>
      <p className="m-0 font-normal text-[13px] text-[#747578] line-clamp-2 mb-[12px]">
        {item.excerpt}
      </p>
      <div className="flex items-center gap-[12px] text-[#A6A6A6] text-[12px]">
        <span className="flex items-center gap-[4px]">
          <FaClock className="text-[10px]" /> {item.date}
        </span>
        <span className="flex items-center gap-[4px]">
          <FaEye className="text-[10px]" /> {item.views.toLocaleString('vi-VN')}
        </span>
      </div>
    </div>
  </Link>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const NewsListingContent = () => {
  const [activeCategory, setActiveCategory] = useState('all');

  const handleCategoryChange = useCallback((key: string) => {
    setActiveCategory(key);
  }, []);

  const filteredNews = activeCategory === 'all'
    ? NEWS_ITEMS
    : NEWS_ITEMS.filter((item) => item.category === activeCategory);

  const [featuredItem, ...restItems] = filteredNews;

  return (
    <section className="news-listing py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        {/* Category tabs */}
        <CategoryTabs
          categories={CATEGORIES}
          activeKey={activeCategory}
          onChange={handleCategoryChange}
        />

        {featuredItem ? (
          <>
            {/* Featured article */}
            <div className="mb-[30px] sm:mb-[40px]">
              <FeaturedNewsCard item={featuredItem} />
            </div>

            {/* News grid */}
            {restItems.length > 0 && (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-[16px] sm:gap-[24px]">
                {restItems.map((item) => (
                  <NewsCard key={item.id} item={item} />
                ))}
              </div>
            )}

            <BoxPagination />
          </>
        ) : (
          <p className="text-center text-[#8C8C8C] text-[14px] py-[60px]">
            Không có bài viết nào trong danh mục này.
          </p>
        )}
      </div>
    </section>
  );
};
