import Link from "next/link";

export const Logo = () => {
  return (
    <div className="inner-logo">
      <Link href="/">
        <img
          src="/assets/images/logo.png"
          alt="28.TRAVEL"
          className="h-[50px] w-auto"
        />
      </Link>
    </div>
  );
};
