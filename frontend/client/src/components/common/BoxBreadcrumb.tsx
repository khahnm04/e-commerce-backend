import Link from 'next/link';
import { FaAnglesRight } from 'react-icons/fa6';

interface BreadcrumbItem {
  title: string;
  link: string;
}

interface BoxBreadcrumbProps {
  title: string;
  list: BreadcrumbItem[];
  image?: string;
}

export const BoxBreadcrumb = ({
  title,
  list,
  image = '/assets/images/banner-5.jpg',
}: BoxBreadcrumbProps) => {
  return (
    <div className="box-breadcrumb relative">
      <div className="inner-image h-[253px] w-full">
        <img
          src={image}
          alt={title}
          className="w-full h-full object-cover"
        />
      </div>
      <div className="inner-content absolute top-0 left-0 w-full h-full flex flex-col justify-end pb-[15px] lg:pb-[20px] bg-[linear-gradient(180deg,rgba(255,255,255,0)_0%,rgba(69,2,199,0.8)_100%)]">
        <div className="container mx-auto px-4">
          <h1 className="inner-title font-medium text-[20px] lg:text-[32px] text-white mt-0 mb-[8px]">
            {title}
          </h1>
          <nav className="inner-links">
            <ul className="list-none p-0 m-0 flex flex-wrap gap-[5px_10px] lg:gap-[5px_16px]">
              {list.map((item, index) => (
                <li key={index}>
                  <Link
                    href={item.link}
                    className="font-normal text-[11px] lg:text-[16px] text-white flex items-center"
                  >
                    {item.title}
                    {index < list.length - 1 && (
                      <FaAnglesRight className="ml-[10px] lg:ml-[16px] text-[8px] lg:text-[10px]" />
                    )}
                  </Link>
                </li>
              ))}
            </ul>
          </nav>
        </div>
      </div>
    </div>
  );
};
