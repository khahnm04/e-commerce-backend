'use client';

import Link from 'next/link';
import { FaClock, FaEye, FaFacebook, FaLink, FaTag, FaXTwitter } from 'react-icons/fa6';

/* ------------------------------------------------------------------ */
/*  Types & Constants                                                 */
/* ------------------------------------------------------------------ */

interface RelatedPost {
  slug: string;
  image: string;
  title: string;
  date: string;
}

const RELATED_POSTS: RelatedPost[] = [
  {
    slug: 'homestay-co-tich-da-lat',
    image: '/assets/images/blog-1.jpg',
    title: 'Homestay "cổ tích" giữa Đà Lạt mộng mơ',
    date: '23/05/2024',
  },
  {
    slug: 'dao-phu-quy-binh-thuan',
    image: '/assets/images/blog-4.jpg',
    title: 'Đảo Phú Quý Bình Thuận hot trend năm 2024',
    date: '15/05/2024',
  },
  {
    slug: 'trekking-cam-trai-da-lat',
    image: '/assets/images/blog-5.jpg',
    title: 'Trekking cắm trại tại Đà Lạt',
    date: '12/05/2024',
  },
];

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const ShareButtons = () => (
  <div className="flex items-center gap-[12px]">
    <span className="font-medium text-[14px] text-[#52575C]">Chia sẻ:</span>
    {[
      { icon: FaFacebook, label: 'Facebook', color: '#1877F2' },
      { icon: FaXTwitter, label: 'Twitter', color: '#000' },
      { icon: FaLink, label: 'Copy link', color: 'var(--color-primary)' },
    ].map(({ icon: Icon, label, color }) => (
      <button
        key={label}
        type="button"
        aria-label={`Chia sẻ qua ${label}`}
        className="w-[36px] h-[36px] rounded-full border border-[#E0E0E0] bg-white inline-flex items-center justify-center cursor-pointer hover:border-transparent hover:text-white transition-all"
        style={{ '--hover-bg': color } as React.CSSProperties}
        onMouseEnter={(e) => {
          (e.currentTarget.style.backgroundColor = color);
          e.currentTarget.style.color = '#fff';
        }}
        onMouseLeave={(e) => {
          e.currentTarget.style.backgroundColor = '';
          e.currentTarget.style.color = '';
        }}
      >
        <Icon className="text-[14px]" />
      </button>
    ))}
  </div>
);

const RelatedPostCard = ({ post }: { post: RelatedPost }) => (
  <Link
    href={`/news/${post.slug}`}
    className="flex gap-[12px] group"
  >
    <div className="w-[100px] h-[70px] rounded-[8px] overflow-hidden shrink-0">
      <img
        src={post.image}
        alt={post.title}
        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
      />
    </div>
    <div className="flex-1">
      <h4 className="m-0 font-medium text-[14px] text-[#25282B] line-clamp-2 group-hover:text-[var(--color-primary)] transition-colors leading-[140%]">
        {post.title}
      </h4>
      <span className="flex items-center gap-[4px] text-[12px] text-[#A6A6A6] mt-[4px]">
        <FaClock className="text-[10px]" /> {post.date}
      </span>
    </div>
  </Link>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const NewsDetailContent = () => {
  return (
    <section className="news-detail py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        <div className="flex flex-wrap lg:flex-nowrap gap-[30px]">
          {/* Main article */}
          <article className="w-full lg:w-[calc(100%-340px)]">
            {/* Meta */}
            <div className="flex flex-wrap items-center gap-[12px] mb-[16px]">
              <span className="inline-flex items-center gap-[4px] bg-[var(--color-primary)]/10 text-[var(--color-primary)] text-[12px] font-medium px-[10px] py-[4px] rounded-[4px]">
                <FaTag className="text-[10px]" /> Mẹo Hay
              </span>
              <span className="flex items-center gap-[4px] text-[13px] text-[#A6A6A6]">
                <FaClock className="text-[11px]" /> 23/05/2024
              </span>
              <span className="flex items-center gap-[4px] text-[13px] text-[#A6A6A6]">
                <FaEye className="text-[11px]" /> 1.250 lượt xem
              </span>
            </div>

            {/* Title */}
            <h2 className="mt-0 mb-[20px] font-bold text-[24px] sm:text-[32px] text-[#25282B] leading-[130%]">
              Top 5 điểm check in độc đáo tại Singapore
            </h2>

            {/* Featured image */}
            <div className="rounded-[12px] overflow-hidden mb-[24px]">
              <img
                src="/assets/images/blog-2.jpg"
                alt="Top 5 điểm check in độc đáo tại Singapore"
                className="w-full h-auto"
              />
            </div>

            {/* Article body */}
            <div className="prose max-w-none text-[15px] text-[#333] leading-[180%]">
              <p>
                Nếu bạn nghĩ du lịch Singapore rất nhàm chán vì chỉ mãi quanh quẩn trong những khối bê tông vô cảm thì bạn &ldquo;chắc chưa?&rdquo;. Tuy đảo quốc Sư Tử không phải là một quốc gia rộng lớn nhưng các địa điểm du lịch tại Singapore chưa từng đánh mất sức hút đối với cộng đồng xê dịch quốc tế.
              </p>

              <h3 className="font-bold text-[20px] text-[var(--color-primary)] mt-[28px] mb-[12px]">
                1. Gardens by the Bay
              </h3>
              <p>
                Gardens by the Bay là khu vườn rộng 101 hecta nằm ngay trung tâm Singapore. Nơi đây nổi tiếng với những &ldquo;siêu cây&rdquo; Supertree Grove cao từ 25 đến 50 mét, được thiết kế theo công nghệ sinh thái hiện đại. Vào buổi tối, show trình diễn ánh sáng &ldquo;Garden Rhapsody&rdquo; biến nơi đây thành một thế giới cổ tích lung linh.
              </p>

              <h3 className="font-bold text-[20px] text-[var(--color-primary)] mt-[28px] mb-[12px]">
                2. Marina Bay Sands SkyPark
              </h3>
              <p>
                Tầng thượng của khách sạn Marina Bay Sands mang đến tầm nhìn 360 độ bao quát toàn bộ thành phố. Đây là điểm check-in mà bất kỳ ai đến Singapore cũng không thể bỏ qua, đặc biệt là vào lúc hoàng hôn khi thành phố dần lên đèn.
              </p>

              <h3 className="font-bold text-[20px] text-[var(--color-primary)] mt-[28px] mb-[12px]">
                3. Haji Lane
              </h3>
              <p>
                Con phố nhỏ đầy màu sắc với những bức tranh tường graffiti nghệ thuật, các cửa hàng vintage và quán cà phê phong cách. Haji Lane là thiên đường cho những ai yêu thích nghệ thuật đường phố và muốn có những bức ảnh cực chất.
              </p>

              <h3 className="font-bold text-[20px] text-[var(--color-primary)] mt-[28px] mb-[12px]">
                4. Jewel Changi Airport
              </h3>
              <p>
                Sân bay Changi không chỉ nổi tiếng về sự hiện đại mà còn sở hữu thác nước trong nhà cao nhất thế giới — Rain Vortex. Khu phức hợp Jewel với thiết kế mái vòm kính ấn tượng là điểm đến du lịch không thể bỏ qua.
              </p>

              <h3 className="font-bold text-[20px] text-[var(--color-primary)] mt-[28px] mb-[12px]">
                5. Sentosa Island
              </h3>
              <p>
                Đảo Sentosa là nơi quy tụ những trải nghiệm giải trí hàng đầu: Universal Studios, S.E.A Aquarium, bãi biển Siloso và nhiều hoạt động thú vị khác. Đây là điểm đến lý tưởng cho cả gia đình và nhóm bạn.
              </p>
            </div>

            {/* Share */}
            <div className="mt-[30px] pt-[20px] border-t border-[#ECECEC]">
              <ShareButtons />
            </div>
          </article>

          {/* Sidebar */}
          <aside className="w-full lg:w-[310px] shrink-0">
            {/* Related posts */}
            <div className="bg-white rounded-[12px] shadow-[0px_1px_6px_rgba(0,0,0,0.08)] p-[20px]">
              <h3 className="m-0 font-semibold text-[18px] text-[var(--color-primary)] mb-[20px] pb-[12px] border-b border-[#ECECEC]">
                Bài Viết Liên Quan
              </h3>
              <div className="flex flex-col gap-[16px]">
                {RELATED_POSTS.map((post) => (
                  <RelatedPostCard key={post.slug} post={post} />
                ))}
              </div>
            </div>

            {/* Tags */}
            <div className="bg-white rounded-[12px] shadow-[0px_1px_6px_rgba(0,0,0,0.08)] p-[20px] mt-[20px]">
              <h3 className="m-0 font-semibold text-[18px] text-[var(--color-primary)] mb-[16px] pb-[12px] border-b border-[#ECECEC]">
                Tags
              </h3>
              <div className="flex flex-wrap gap-[8px]">
                {['Singapore', 'Du lịch', 'Check-in', 'Châu Á', 'Tips'].map((tag) => (
                  <Link
                    key={tag}
                    href="#"
                    className="px-[12px] py-[6px] bg-[#F5F5F5] rounded-[6px] text-[13px] text-[#52575C] font-normal hover:bg-[var(--color-primary)] hover:text-white transition-all"
                  >
                    {tag}
                  </Link>
                ))}
              </div>
            </div>
          </aside>
        </div>
      </div>
    </section>
  );
};
