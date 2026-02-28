import { CartItems } from "@/app/(page)/cart/_components/CartItems";
import { CheckoutForm } from "@/app/(page)/cart/_components/CheckoutForm";

export default function CartPage() {
  return (
    <>
      <CartItems />
      <CheckoutForm />
    </>
  );
}
