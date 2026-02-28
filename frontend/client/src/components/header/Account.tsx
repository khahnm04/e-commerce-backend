import Link from "next/link";
import { FaRegCircleUser } from "react-icons/fa6";

export const Account = () => {
    return (
        <div className="inner-account">
            <Link
                href="/account"
                className="inline-flex items-center"
            >
                <FaRegCircleUser className="text-[26px] text-black" />
            </Link>
        </div>
    );
};
