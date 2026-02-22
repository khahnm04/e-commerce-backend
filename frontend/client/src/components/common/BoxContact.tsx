import Link from 'next/link';
import { FaFacebookF, FaInstagram, FaWhatsapp, FaYoutube } from 'react-icons/fa6';

export const BoxContact = () => {
    return (
        <nav className="box-contact fixed right-[20px] bottom-[50px] z-[900] bg-[var(--color-primary)] rounded-[10px] shadow-[0px_2px_4px_0px_#00000026] p-[12px_10px] hidden sm:block">
            <ul className="list-none p-0 m-0 flex flex-col items-center gap-[20px]">
                <li>
                    <Link href="#" target="_blank" className="text-[19px] text-white block">
                        <FaFacebookF />
                    </Link>
                </li>
                <li>
                    <Link href="#" target="_blank" className="text-[19px] text-white block">
                        <FaInstagram />
                    </Link>
                </li>
                <li>
                    <Link href="#" target="_blank" className="text-[19px] text-white block">
                        <FaWhatsapp />
                    </Link>
                </li>
                <li>
                    <Link href="#" target="_blank" className="text-[19px] text-white block">
                        <FaYoutube />
                    </Link>
                </li>
            </ul>
        </nav>
    );
};
