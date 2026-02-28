export const BoxTourSchedule = () => {
  const scheduleItems = [
    {
      title: "NGÀY 1 | TP.HCM – THỦ ĐÔ HÀ NỘI",
      content: (
        <>
          <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
            Lorem ipsum dolor, sit amet consectetur adipisicing elit. Iste illum ipsa voluptate voluptatibus aspernatur, ratione facere illo odit molestias explicabo? Accusamus, consequuntur? Animi eveniet veniam illo a pariatur quia aperiam.
          </p>
          <img
            src="/assets/images/banner-14.jpg"
            alt="Hanoi"
            className="w-full h-auto rounded-[10px] mb-[10px]"
          />
          <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Harum reprehenderit sit laboriosam eos eligendi delectus nemo tempora quibusdam quae iure odio, a quisquam architecto rerum rem similique voluptate nam ducimus.
          </p>
        </>
      )
    },
    {
      title: "NGÀY 2 | TP.HCM – THỦ ĐÔ HÀ NỘI",
      content: (
        <>
          <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
            Lorem ipsum dolor, sit amet consectetur adipisicing elit. Iste illum ipsa voluptate voluptatibus aspernatur, ratione facere illo odit molestias explicabo? Accusamus, consequuntur? Animi eveniet veniam illo a pariatur quia aperiam.
          </p>
          <img
            src="/assets/images/banner-14.jpg"
            alt="Hanoi"
            className="w-full h-auto rounded-[10px] mb-[10px]"
          />
          <p className="font-light text-[14px] text-[#080A12] mt-0 mb-[10px]">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Harum reprehenderit sit laboriosam eos eligendi delectus nemo tempora quibusdam quae iure odio, a quisquam architecto rerum rem similique voluptate nam ducimus.
          </p>
        </>
      )
    }
  ];

  return (
    <div className="box-tour-schedule mt-[30px]">
      <div className="inner-title-main font-semibold text-[20px] text-[var(--color-primary)] mb-[15px]">
        Lịch Trình Tour
      </div>
      <div className="inner-list flex flex-col gap-[15px] relative">
        <div className="inner-line w-[1px] h-full bg-[var(--color-primary)] absolute top-[14px] left-0"></div>

        {scheduleItems.map((item, index) => (
          <div key={index} className="inner-item ml-[15px] relative">
            <div className="inner-dot w-[10px] h-[10px] rounded-full bg-[var(--color-primary)] absolute left-[-19.5px] top-[12px]"></div>
            <div className="inner-title bg-[var(--color-primary)] rounded-[5px] p-[8px] font-medium text-[14px] text-white mb-[10px] inline-block">
              {item.title}
            </div>
            <div className="inner-content bg-white shadow-[0px_1.75px_3.51px_0px_#0000001F] rounded-[10px] p-[15px] sm:p-[20px]">
              {item.content}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
