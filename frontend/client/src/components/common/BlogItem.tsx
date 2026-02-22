import Link from 'next/link';

interface BlogItemProps {
    image?: string;
    date?: string;
    title: string;
    className?: string;
}

export const BlogItem = ({
    image = '/assets/images/blog-1.jpg',
    date = '23/05/2024',
    title,
    className = '',
}: BlogItemProps) => {
    return (
        <div className={`blog-item aspect-[302/308] rounded-[10px] sm:rounded-[20px] overflow-hidden relative ${className}`}>
            <Link href="#">
                <div className="inner-image w-full h-full">
                    <img
                        src={image}
                        alt={title}
                        className="w-full h-full object-cover"
                    />
                </div>
                <div className="inner-content absolute top-0 left-0 w-full h-full p-[10px] sm:p-[18px_24px] flex flex-col justify-end gap-[6px] sm:gap-[10px] bg-[linear-gradient(180deg,rgba(69,2,199,0)_58.85%,rgba(69,2,199,0.9)_100%)]">
                    <div className="inner-date font-normal text-[10px] sm:text-[12px] text-white">
                        {date}
                    </div>
                    <h3 className="inner-title m-0 text-white font-normal text-[14px] sm:text-[18px] overflow-hidden line-clamp-2">
                        {title}
                    </h3>
                </div>
            </Link>
        </div>
    );
};
