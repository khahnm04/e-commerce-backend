import Link from 'next/link'

export default function NotFound() {
  return (
    <>
      return (
      <main className="
      absolute 
      top-[70px] 
      left-0 
      w-full 
      min-h-[calc(100vh-70px)] 
      bg-[#F4F5F9] 
      p-[30px] 
      
      max-[575.98px]:px-[15px] /* Tương đương @media max-width 575px */
      lg:left-[240px]           /* Tương đương min-width 992px (logic ngược của max-width 991px) */
      lg:w-[calc(100%-240px)]
    ">
        <div className="
        border border-[#B9B9B9] 
        bg-white 
        rounded-[14px] 
        text-center
        
        /* Mobile styles (mặc định) */
        p-[40px_30px]
        
        /* Desktop styles (md trở lên ~ 768px) */
        md:p-[80px]
      ">
          {/* Image Section */}
          {/* Lưu ý: Thay đường dẫn src phù hợp với project Nextjs của bạn */}
          <img
            src="/assets/images/error-404.svg"
            alt="Error 404"
            className="
            mx-auto 
            h-auto
            
            w-[60%]          /* Mobile */
            mb-[30px]        /* Mobile */
            
            md:w-[390px]     /* Desktop */
            md:mb-[60px]     /* Desktop */
          "
          />

          {/* Title Section */}
          <h1 className="
          font-bold 
          text-[var(--color-text)] 
          mt-0
          
          text-[24px]        /* Mobile */
          mb-[20px]          /* Mobile */
          
          md:text-[32px]     /* Desktop */
          md:mb-[30px]       /* Desktop */
        ">
            Trang này không tồn tại...
          </h1>

          {/* Back Button */}
          <Link
            href="/"
            className="
            inline-block 
            bg-[var(--color-primary)] 
            rounded-[8px] 
            opacity-90 
            font-bold 
            text-white 
            hover:opacity-100
            transition-opacity  /* Thêm hiệu ứng mượt mà */
            
            py-[15px] 
            px-[40px]         /* Mobile */
            text-[16px]       /* Mobile */
            
            md:px-[104px]     /* Desktop */
            md:text-[20px]    /* Desktop */
          "
          >
            Trở về trang tổng quan
          </Link>
        </div>
      </main>
      );
    </>
  )
}