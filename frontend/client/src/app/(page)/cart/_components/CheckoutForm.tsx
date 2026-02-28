'use client';

import { useState, useCallback } from 'react';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface InputFieldConfig {
  id: string;
  name: string;
  type: string;
  placeholder: string;
  colSpan?: boolean;
}

interface PaymentMethodOption {
  value: string;
  id: string;
  label: string;
}

interface BankInfo {
  label: string;
  value: string;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const CUSTOMER_FIELDS: InputFieldConfig[] = [
  { id: 'full-name-input', name: 'fullName', type: 'text', placeholder: 'Họ và tên *' },
  { id: 'phone-input', name: 'phone', type: 'tel', placeholder: 'Số điện thoại *' },
];

const PAYMENT_METHODS: PaymentMethodOption[] = [
  { value: 'money', id: 'methodMoney', label: 'Thanh toán tiền mặt khi đi tour' },
  { value: 'momo', id: 'methodMomo', label: 'Ví MoMo' },
  { value: 'bank', id: 'methodBank', label: 'Chuyển khoản ngân hàng' },
];

const BANK_INFO: BankInfo[] = [
  { label: 'Ngân hàng', value: 'Vietcombank' },
  { label: 'Tên tài khoản', value: 'Le Van A' },
  { label: 'STK', value: '0123456789' },
];

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const SectionTitle = ({ children }: { children: React.ReactNode }) => (
  <h3 className="m-0 font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)] mb-[25px]">
    {children}
  </h3>
);

const CustomerInfoSection = ({ fields }: { fields: InputFieldConfig[] }) => (
  <div className="mb-[30px]">
    <SectionTitle>Thông Tin Khách Hàng</SectionTitle>
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-[15px_20px]">
      {fields.map(({ id, name, type, placeholder }) => (
        <div key={id}>
          <input
            id={id}
            name={name}
            type={type}
            placeholder={placeholder}
            aria-label={placeholder.replace(' *', '')}
            className="w-full h-[50px] px-[20px] border border-[#E0E0E0] rounded-[6px] outline-none focus:border-[var(--color-primary)] transition-colors"
          />
        </div>
      ))}
      <div className="col-span-1 sm:col-span-2">
        <textarea
          name="note"
          placeholder="Ghi chú"
          aria-label="Ghi chú"
          className="w-full h-[100px] p-[12px_20px] border border-[#E0E0E0] rounded-[6px] outline-none resize-y focus:border-[var(--color-primary)] transition-colors"
        />
      </div>
    </div>
  </div>
);

const BankTransferInfo = ({ info }: { info: BankInfo[] }) => (
  <div className="bg-[#F3F3F3] rounded-[4.25px] mt-[20px] p-[15px] animate-fadeIn">
    <h4 className="m-0 font-semibold text-[16px] text-[var(--color-primary)] mb-[10px]">
      Thông tin chuyển khoản
    </h4>
    <dl className="m-0 flex flex-col gap-[6px] font-normal text-[14px] text-[#4F4F4F]">
      {info.map(({ label, value }) => (
        <div key={label} className="flex gap-[4px]">
          <dt className="m-0">{label}:</dt>
          <dd className="m-0 font-medium">{value}</dd>
        </div>
      ))}
    </dl>
  </div>
);

const PaymentMethodSection = ({
  methods,
  selected,
  onChange,
}: {
  methods: PaymentMethodOption[];
  selected: string;
  onChange: (value: string) => void;
}) => (
  <div className="mb-[30px]">
    <SectionTitle>Chọn phương thức thanh toán</SectionTitle>
    <fieldset className="border-0 p-0 m-0">
      <legend className="sr-only">Phương thức thanh toán</legend>
      <div className="flex flex-col gap-[10px]">
        {methods.map(({ value, id, label }) => (
          <div key={id} className="flex items-center gap-[10px]">
            <input
              type="radio"
              name="paymentMethod"
              value={value}
              id={id}
              checked={selected === value}
              onChange={() => onChange(value)}
              className="w-[21px] h-[21px] m-0 accent-[var(--color-primary)] cursor-pointer"
            />
            <label
              htmlFor={id}
              className="font-normal text-[14px] text-[#333333] cursor-pointer"
            >
              {label}
            </label>
          </div>
        ))}
      </div>
    </fieldset>

    {selected === 'bank' && <BankTransferInfo info={BANK_INFO} />}
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const CheckoutForm = () => {
  const [paymentMethod, setPaymentMethod] = useState('money');

  const handlePaymentChange = useCallback((value: string) => {
    setPaymentMethod(value);
  }, []);

  const handleSubmit = useCallback((e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // TODO: Implement order submission
  }, []);

  return (
    <section className="checkout-form pb-[45px] sm:pb-[60px]">
      <div className="container mx-auto px-4">
        <div className="w-full lg:w-[902px] bg-white shadow-[0px_3.05px_3.81px_0px_#00000026] rounded-[18px] p-[20px_15px] sm:p-[30px_20px] mx-auto">
          <form id="order-form" onSubmit={handleSubmit}>
            <CustomerInfoSection fields={CUSTOMER_FIELDS} />

            <PaymentMethodSection
              methods={PAYMENT_METHODS}
              selected={paymentMethod}
              onChange={handlePaymentChange}
            />

            <div className="text-center">
              <button
                type="submit"
                className="w-full sm:w-[480px] h-[46px] bg-[var(--color-primary)] rounded-[8px] border-0 font-semibold text-[20px] text-white cursor-pointer hover:brightness-110 transition-all"
              >
                ĐẶT HÀNG
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>
  );
};
