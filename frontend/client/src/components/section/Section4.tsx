import { BoxTitle } from '@/components/common/BoxTitle';
import { ButtonOutline } from '@/components/common/ButtonOutline';
import { ProductItem } from '@/components/product/ProductItem';

export const Section4 = () => {
    return (
        <>
            {/* Section 4 */}
            <div className="section-4 pb-[45px] sm:pb-[60px]">
                <div className="container">
                    <BoxTitle
                        dataAos="fade-up"
                        dataAosDuration="800"
                        dataAosDelay="200"
                    >
                        Tour Trong Nước
                    </BoxTitle>
                    <div
                        className="inner-wrap grid grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[10px] sm:gap-[20px]"
                        data-aos="fade-up"
                        data-aos-duration="800"
                        data-aos-delay="400"
                    >
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                        <ProductItem />
                    </div>
                    <div className="inner-button mt-[25px] sm:mt-[30px] text-center">
                        <ButtonOutline>Xem tất cả</ButtonOutline>
                    </div>
                </div>
            </div>
            {/* End Section 4 */}
        </>
    );
};
