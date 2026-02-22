import Link from "next/link";

export const Account = () => {
    return (
        <div className="inner-account">
            <Link
                href="#"
                className="inline-flex items-center"
            >
                <img
                    src="/assets/images/icon-user.svg"
                    alt="Account"
                    className="h-[26px] w-auto"
                />
            </Link>
        </div>
    );
};
