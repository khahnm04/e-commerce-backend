import Link from 'next/link';

interface BlogItem2Props {
    image?: string;
    date?: string;
    title: string;
    desc: string;
    className?: string;
}

export const BlogItem2 = ({
    image = '/assets/images/blog-2.jpg',
    date = '23/05/2024',
    title,
    desc,
    className = '',
}: BlogItem2Props) => {
    return (
        <div className={`blog-item-2 col-span-2 row-span-2 shadow-[rgba(0,0,0,0.16)_0px_1px_4px] rounded-[10px] sm:rounded-[20px] overflow-hidden bg-white ${className}`}>
            <Link href="#">
                <div className="inner-image aspect-[536/400]">
                    <img
                        src={image}
                        alt={title}
                        className="w-full h-full object-cover"
                    />
                </div>
                <div className="inner-content p-[10px] sm:p-[16px_24px]">
                    <div className="inner-date font-light text-[12px] sm:text-[14px] text-black mb-[10px]">
                        {date}
                    </div>
                    <h3 className="inner-title font-normal text-[18px] sm:text-[22px] text-[var(--color-primary)] mt-0 mb-[10px] overflow-hidden line-clamp-1">
                        {title}
                    </h3>
                    <div className="inner-desc font-light text-[14px] text-black overflow-hidden line-clamp-2">
                        {desc}
                    </div>
                </div>
            </Link>
        </div>
    );
};
