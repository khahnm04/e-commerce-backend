import { BlogItem } from '@/components/common/BlogItem';
import { BlogItem2 } from '@/components/common/BlogItem2';
import { BoxTitle } from '@/components/common/BoxTitle';

export const Section6 = () => {
    return (
        <>
            {/* Section 6 */}
            <div className="section-6 pb-[45px] sm:pb-[60px]">
                <div className="container">
                    <BoxTitle
                        dataAos="fade-up"
                        dataAosDuration="800"
                        dataAosDelay="200"
                    >
                        Tin Tức Mới
                    </BoxTitle>
                    <div
                        className="inner-wrap grid grid-cols-2 lg:grid-cols-4 gap-[10px] sm:gap-[24px_20px]"
                        data-aos="fade-up"
                        data-aos-duration="800"
                        data-aos-delay="400"
                    >
                        <BlogItem
                            image="/assets/images/blog-1.jpg"
                            title="Homestay “cổ tích” giữa Đà Lạt mộng mơ"
                        />
                        <BlogItem
                            image="/assets/images/blog-3.jpg"
                            title="Đặc sản không thể bỏ qua khi đến với Đà Nẵng - Hội An"
                            className="lg:col-start-4 lg:row-start-1"
                        />
                        <BlogItem2
                            image="/assets/images/blog-2.jpg"
                            title="Top 5 điểm check in độc đáo tại Singapore"
                            desc="Nếu bạn nghĩ du lịch Singapore rất nhàm chán vì chỉ mãi quanh quẩn trong những khối bê tông vô cảm thì bạn “chắc chưa?”. Tuy đảo quốc Sư Tử không phải là một quốc gia rộng lớn nhưng các địa điểm du lịch tại Singapore chưa từng đánh mất sức hút đối với cộng đồng xê dịch quốc tế."
                            className="lg:col-start-2 lg:row-start-1"
                        />
                        <BlogItem
                            image="/assets/images/blog-4.jpg"
                            title="Đảo Phú Quý Bình Thuận hot trend năm 2024"
                        />
                        <BlogItem
                            image="/assets/images/blog-5.jpg"
                            title="Trekking cắm trại tại Đà Lạt- Cảm nhận Đà Lạt qua từng bước chân"
                        />
                    </div>
                </div>
            </div>
            {/* End Section 6 */}
        </>
    );
};
