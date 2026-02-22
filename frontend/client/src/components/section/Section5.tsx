import Link from 'next/link';

export const Section5 = () => {
    return (
        <>
            {/* Section 5 */}
            <div
                className="section-5 pb-[45px] sm:pb-[60px]"
                data-aos="fade-up"
                data-aos-duration="800"
                data-aos-delay="200"
            >
                <div className="container">
                    <div className="inner-image rounded-[10px] overflow-hidden">
                        <Link href="#">
                            <img
                                src="/assets/images/banner-4.png"
                                alt=""
                                className="w-full h-auto"
                            />
                        </Link>
                    </div>
                </div>
            </div>
            {/* End Section 5 */}
        </>
    );
};
