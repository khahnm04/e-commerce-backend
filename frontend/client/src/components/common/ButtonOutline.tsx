import Link from 'next/link';

interface ButtonOutlineProps {
    children: React.ReactNode;
    href?: string;
    className?: string;
}

export const ButtonOutline = ({
    children,
    href = '#',
    className = '',
}: ButtonOutlineProps) => {
    return (
        <Link
            href={href}
            className={`button-outline inline-block border-2 border-[var(--color-primary)] rounded-[6px] bg-white p-[12px_20px] sm:p-[13px_25px] font-medium text-[14px] sm:text-[16px] text-[var(--color-primary)] hover:bg-[var(--color-primary)] hover:text-white transition-colors duration-300 ${className}`}
        >
            {children}
        </Link>
    );
};
