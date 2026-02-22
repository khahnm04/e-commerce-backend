"use client"
import { useState } from 'react';

export const Section10 = () => {
  const [paymentMethod, setPaymentMethod] = useState('money');

  return (
    <div className="section-10 pb-[45px] sm:pb-[60px]">
      <div className="container mx-auto px-4">
        <div className="inner-wrap w-full lg:w-[902px] bg-white shadow-[0px_3.05px_3.81px_0px_#00000026] rounded-[18px] p-[20px_15px] sm:p-[30px_20px] mx-auto">
          <form id="order-form">
            <div className="inner-info mb-[30px]">
              <div className="inner-title-main font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)] mb-[25px]">
                Thông Tin Khách Hàng
              </div>
              <div className="inner-list grid grid-cols-1 sm:grid-cols-2 gap-[15px_20px]">
                <div className="inner-item">
                  <input
                    id="full-name-input"
                    name="fullName"
                    type="text"
                    placeholder="Họ và tên *"
                    className="w-full h-[50px] px-[20px] border border-[#E0E0E0] rounded-[6px] outline-none"
                  />
                </div>
                <div className="inner-item">
                  <input
                    id="phone-input"
                    name="phone"
                    type="text"
                    placeholder="Số điện thoại *"
                    className="w-full h-[50px] px-[20px] border border-[#E0E0E0] rounded-[6px] outline-none"
                  />
                </div>
                <div className="inner-item col-span-1 sm:col-span-2">
                  <textarea
                    placeholder="Ghi chú"
                    name="note"
                    className="w-full h-[100px] p-[12px_20px] border border-[#E0E0E0] rounded-[6px] outline-none"
                  ></textarea>
                </div>
              </div>
            </div>

            <div className="inner-method mb-[30px]">
              <div className="inner-title-main font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)] mb-[25px]">
                Chọn phương thức thanh toán
              </div>
              <div className="inner-list flex flex-col gap-[10px]">
                <div className="inner-item flex items-center gap-[10px]">
                  <input
                    type="radio"
                    name="method"
                    value="money"
                    id="methodMoney"
                    checked={paymentMethod === 'money'}
                    onChange={() => setPaymentMethod('money')}
                    className="w-[21px] h-[21px] m-0 accent-[var(--color-primary)]"
                  />
                  <label htmlFor="methodMoney" className="font-normal text-[14px] text-[#333333]">Thanh toán tiền mặt khi đi tour</label>
                </div>
                <div className="inner-item flex items-center gap-[10px]">
                  <input
                    type="radio"
                    name="method"
                    value="momo"
                    id="methodMomo"
                    checked={paymentMethod === 'momo'}
                    onChange={() => setPaymentMethod('momo')}
                    className="w-[21px] h-[21px] m-0 accent-[var(--color-primary)]"
                  />
                  <label htmlFor="methodMomo" className="font-normal text-[14px] text-[#333333]">Ví MoMo</label>
                </div>
                <div className="inner-item flex items-center gap-[10px]">
                  <input
                    type="radio"
                    name="method"
                    value="bank"
                    id="methodBank"
                    checked={paymentMethod === 'bank'}
                    onChange={() => setPaymentMethod('bank')}
                    className="w-[21px] h-[21px] m-0 accent-[var(--color-primary)]"
                  />
                  <label htmlFor="methodBank" className="font-normal text-[14px] text-[#333333]">Chuyển khoản ngân hàng</label>
                </div>
              </div>

              {paymentMethod === 'bank' && (
                <div className="inner-info-bank bg-[#F3F3F3] rounded-[4.25px] mt-[20px] p-[15px] block animate-fadeIn">
                  <div className="inner-title font-semibold text-[16px] text-[var(--color-primary)] mb-[10px]">
                    Thông tin chuyển khoản
                  </div>
                  <div className="inner-info-list flex flex-col gap-[6px] font-normal text-[14px] text-[#4F4F4F]">
                    <div>Ngân hàng : Vietcombank</div>
                    <div>Tên tài khoản: Le Van A</div>
                    <div>STK: 0123456789</div>
                  </div>
                </div>
              )}
            </div>

            <div className="inner-button text-center">
              <button className="w-full sm:w-[480px] h-[46px] bg-[var(--color-primary)] rounded-[8px] border-0 font-semibold text-[20px] text-white cursor-pointer hover:brightness-110 transition-all">
                ĐẶT TOUR
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
