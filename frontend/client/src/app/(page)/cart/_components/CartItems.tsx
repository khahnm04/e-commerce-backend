'use client';

import { useState, useCallback } from 'react';
import Link from 'next/link';
import { FaAngleRight, FaXmark } from 'react-icons/fa6';

/* ------------------------------------------------------------------ */
/*  Types                                                              */
/* ------------------------------------------------------------------ */

interface CartQuantity {
  label: string;
  count: number;
  unitPrice: number;
}

interface CartItemData {
  id: number;
  title: string;
  code: string;
  departureDate: string;
  departureLocation: string;
  image: string;
  quantities: CartQuantity[];
}

interface PriceSummaryRow {
  label: string;
  value: string;
  isHighlighted?: boolean;
}

/* ------------------------------------------------------------------ */
/*  Mock data                                                          */
/* ------------------------------------------------------------------ */

const MOCK_CART_ITEMS: CartItemData[] = [
  {
    id: 1,
    title: 'Tour Châu Âu Đón Noel | 11N10D | Pháp – Thụy Sĩ – Ý',
    code: '123456789',
    departureDate: '20/10/2024',
    departureLocation: 'Hà Nội',
    image: '/assets/images/banner-15.jpg',
    quantities: [
      { label: 'Người lớn', count: 1, unitPrice: 10_000_000 },
      { label: 'Trẻ em', count: 0, unitPrice: 10_000_000 },
      { label: 'Em bé', count: 0, unitPrice: 10_000_000 },
    ],
  },
  {
    id: 2,
    title: 'Tour Châu Âu Đón Noel | 11N10D | Pháp – Thụy Sĩ – Ý',
    code: '123456789',
    departureDate: '20/10/2024',
    departureLocation: 'Hà Nội',
    image: '/assets/images/banner-15.jpg',
    quantities: [
      { label: 'Người lớn', count: 1, unitPrice: 10_000_000 },
      { label: 'Trẻ em', count: 0, unitPrice: 10_000_000 },
      { label: 'Em bé', count: 0, unitPrice: 10_000_000 },
    ],
  },
];

const PRICE_SUMMARY: PriceSummaryRow[] = [
  { label: 'Tổng tiền:', value: '15.070.000 đ' },
  { label: 'Giảm:', value: '-5.070.000 đ' },
  { label: 'Thanh toán:', value: '17.070.000 đ', isHighlighted: true },
];

const formatPrice = (price: number): string =>
  price.toLocaleString('vi-VN');

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const CartItemActions = ({
  checked,
  onToggle,
  onRemove,
}: {
  checked: boolean;
  onToggle: () => void;
  onRemove: () => void;
}) => (
  <div className="mr-0 lg:mr-[6px] w-full lg:w-[21px] flex lg:block justify-between lg:justify-start mb-[10px] lg:mb-0">
    <button
      type="button"
      aria-label="Xóa sản phẩm"
      className="order-1 lg:order-none w-[21px] h-[21px] inline-flex items-center justify-center text-[16px] bg-transparent border-0 cursor-pointer text-[#828282] hover:text-red-500 transition-colors"
      onClick={onRemove}
    >
      <FaXmark />
    </button>
    <input
      type="checkbox"
      checked={checked}
      onChange={onToggle}
      aria-label="Chọn sản phẩm"
      className="w-[21px] h-[21px] mt-0 lg:mt-[43px] accent-[var(--color-primary)] cursor-pointer"
    />
  </div>
);

const CartItemProduct = ({
  title,
  code,
  departureDate,
  departureLocation,
  image,
}: Omit<CartItemData, 'id' | 'quantities'>) => {
  const metaItems = [
    { label: 'Mã Tour', value: code },
    { label: 'Ngày Khởi Hành', value: departureDate },
    { label: 'Khởi Hành Tại', value: departureLocation },
  ];

  return (
    <div className="w-full lg:w-[59%] flex items-start mb-[10px] lg:mb-0">
      <div className="w-[118px] sm:w-[178px] aspect-[178/149] rounded-[8px] overflow-hidden mr-[10px] sm:mr-[15px] shrink-0">
        <img
          src={image}
          alt={title}
          className="w-full h-full object-cover"
        />
      </div>
      <div className="flex-1">
        <div className="mb-[5px] sm:mb-[12px]">
          <Link
            href="/products"
            className="font-medium text-[14px] sm:text-[16px] text-[#25282B] overflow-hidden line-clamp-2 hover:text-[var(--color-primary)] transition-colors"
          >
            {title}
          </Link>
        </div>
        <div className="flex flex-col gap-[3px] sm:gap-[5px]">
          {metaItems.map(({ label, value }) => (
            <p
              key={label}
              className="m-0 font-normal text-[12px] sm:text-[14px] text-[#8C8C8C]"
            >
              {label}: <b className="font-semibold text-[#8C8C8C]">{value}</b>
            </p>
          ))}
        </div>
      </div>
    </div>
  );
};

const CartItemQuantity = ({ quantities }: { quantities: CartQuantity[] }) => (
  <div className="flex-1 ml-0 lg:ml-[10px]">
    <p className="m-0 font-normal text-[14px] text-[#5D5E60] mb-[8px] text-left lg:text-right">
      Số Lượng Hành Khách
    </p>
    <div className="flex flex-col gap-[8px]">
      {quantities.map(({ label, count, unitPrice }) => (
        <div key={label} className="flex items-center">
          <span className="font-normal text-[14px] text-[#5D5E60] w-[100px] mr-[16px]">
            {label}:
          </span>
          <span className="flex-1">
            <input
              type="number"
              defaultValue={count}
              min={0}
              aria-label={`Số lượng ${label}`}
              className="w-[40px] border-0 outline-none font-medium text-[14px] text-[#898B8D] border-b border-dotted border-[#ddd]"
            />
          </span>
          <span className="font-normal text-[16px] text-[#898B8D]">
            {count} x{' '}
            <span className="text-[var(--color-primary)]">
              {formatPrice(unitPrice)}
            </span>
          </span>
        </div>
      ))}
    </div>
  </div>
);

const CartItemRow = ({
  item,
  checked,
  onToggle,
  onRemove,
}: {
  item: CartItemData;
  checked: boolean;
  onToggle: () => void;
  onRemove: () => void;
}) => (
  <div className="pb-[10px] sm:pb-[15px] border-b border-[#F2F2F2] mb-[15px] flex flex-wrap lg:flex-nowrap items-start last:border-b-0 last:mb-0">
    <CartItemActions checked={checked} onToggle={onToggle} onRemove={onRemove} />
    <CartItemProduct
      title={item.title}
      code={item.code}
      departureDate={item.departureDate}
      departureLocation={item.departureLocation}
      image={item.image}
    />
    <CartItemQuantity quantities={item.quantities} />
  </div>
);

const CouponForm = () => {
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  return (
    <div className="mb-[15px] flex justify-end">
      <form
        onSubmit={handleSubmit}
        className="w-full lg:w-[429px] flex relative"
      >
        <input
          id="coupon-input"
          name="coupon"
          type="text"
          placeholder="Nhập mã giảm giá"
          className="flex-1 h-[40px] rounded-l-[5px] border border-[#9EA0A3] font-normal text-[14px] text-[#333333] px-[12px] outline-none"
        />
        <button
          type="submit"
          className="h-[40px] rounded-r-[5px] bg-[var(--color-primary)] px-[24px] border-0 font-normal text-[14px] text-white cursor-pointer hover:brightness-110 transition-all"
        >
          Dùng Mã
        </button>
      </form>
    </div>
  );
};

const PriceSummary = ({ rows }: { rows: PriceSummaryRow[] }) => (
  <div className="flex flex-col gap-[16px]">
    {rows.map(({ label, value, isHighlighted }) => (
      <div key={label} className="flex items-center justify-between">
        <span className="font-normal text-[14px] text-[#333333]">
          {label}
        </span>
        <span
          className={
            isHighlighted
              ? 'font-bold text-[22px] text-[var(--color-primary)]'
              : 'font-normal text-[17px] text-[#333333]'
          }
        >
          {value}
        </span>
      </div>
    ))}
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                     */
/* ------------------------------------------------------------------ */

export const CartItems = () => {
  const [cartItems, setCartItems] = useState<CartItemData[]>(MOCK_CART_ITEMS);
  const [selectedIds, setSelectedIds] = useState<Set<number>>(new Set());

  const handleToggleSelect = useCallback((id: number) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  }, []);

  const handleRemoveItem = useCallback((id: number) => {
    setCartItems((prev) => prev.filter((item) => item.id !== id));
    setSelectedIds((prev) => {
      const next = new Set(prev);
      next.delete(id);
      return next;
    });
  }, []);

  return (
    <section className="cart-items py-[45px] sm:pt-[45px] pb-[30px] lg:pt-[60px] lg:pb-[30px]">
      <div className="container mx-auto px-4">
        <div className="w-full lg:w-[902px] bg-white shadow-[0px_3.05px_3.81px_0px_#00000026] rounded-[18px] p-[20px_15px] sm:p-[30px_20px] mx-auto">
          {/* Header */}
          <div className="flex items-center justify-between mb-[25px]">
            <h2 className="m-0 font-semibold text-[16px] sm:text-[20px] text-[var(--color-primary)]">
              Giỏ Hàng
            </h2>
            <Link
              href="/products"
              className="font-normal text-[12px] sm:text-[15px] text-[#52575C] inline-flex items-center gap-[5px] hover:text-[var(--color-primary)] transition-colors"
            >
              Quay lại mua hàng <FaAngleRight className="text-[9px]" />
            </Link>
          </div>

          {/* Cart item list */}
          <div className="mb-[15px]">
            {cartItems.length > 0 ? (
              cartItems.map((item) => (
                <CartItemRow
                  key={item.id}
                  item={item}
                  checked={selectedIds.has(item.id)}
                  onToggle={() => handleToggleSelect(item.id)}
                  onRemove={() => handleRemoveItem(item.id)}
                />
              ))
            ) : (
              <p className="text-center text-[#8C8C8C] text-[14px] py-[40px]">
                Giỏ hàng của bạn đang trống.
              </p>
            )}
          </div>

          {/* Coupon */}
          <CouponForm />

          {/* Price summary */}
          <PriceSummary rows={PRICE_SUMMARY} />
        </div>
      </div>
    </section>
  );
};
