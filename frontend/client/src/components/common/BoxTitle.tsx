import { ReactNode } from 'react';

interface BoxTitleProps {
    children: ReactNode;
    dataAos?: string;
    dataAosDuration?: string;
    dataAosDelay?: string;
}

export const BoxTitle = ({
    children,
    dataAos,
    dataAosDuration,
    dataAosDelay,
}: BoxTitleProps) => {
    return (
        <h2
            className="box-title mt-0 mb-[25px] lg:mb-[30px] font-bold text-[24px] lg:text-[36px] text-[var(--color-primary)] text-center"
            data-aos={dataAos}
            data-aos-duration={dataAosDuration}
            data-aos-delay={dataAosDelay}
        >
            {children}
        </h2>
    );
};
