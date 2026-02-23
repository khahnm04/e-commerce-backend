"use client"
import Link from 'next/link'
import { useState } from 'react'
import { Header } from '@/components/header/Header'
import { Sider } from '@/components/sider/Sider'

export default function NotFound() {
  const [activeSider, setActiveSider] = useState(false);

  return (
    <>
      {/* Header */}
      <Header
        activeSider={activeSider}
        setActiveSider={setActiveSider}
      />
      {/* End Header */}

      {/* Sider */}
      <Sider
        activeSider={activeSider}
        setActiveSider={setActiveSider}
      />
      {/* End Sider */}

      <main className="
        absolute 
        top-[70px] 
        left-0 
        w-full 
        min-h-[calc(100vh-70px)] 
        bg-[#F4F5F9] 
        p-[30px] 
        
        max-[575.98px]:px-[15px]
        lg:left-[240px]
        lg:w-[calc(100%-240px)]
      ">
        <div className="
          border border-[#B9B9B9] 
          bg-white 
          rounded-[14px] 
          text-center

          p-[40px_30px]

          md:p-[80px]
        ">
          {/* Image Section */}
          <img
            src="/assets/images/error-404.svg"
            alt="Error 404"
            className="
              mx-auto 
              h-auto

              w-[60%]
              mb-[30px]

              md:w-[390px]
              md:mb-[60px]
            "
          />

          {/* Title Section */}
          <h1 className="
            font-bold 
            text-[var(--color-text)] 
            mt-0

            text-[24px]
            mb-[20px]

            md:text-[32px]
            md:mb-[30px]
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
              transition-opacity

              py-[15px] 
              px-[40px]
              text-[16px]

              md:px-[104px]
              md:text-[20px]
            "
          >
            Trở về trang tổng quan
          </Link>
        </div>
      </main>
    </>
  )
}