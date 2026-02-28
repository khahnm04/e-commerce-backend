'use client';

import { useState, useCallback } from 'react';
import { FaClock, FaEnvelope, FaFacebook, FaInstagram, FaLocationDot, FaPhone, FaXTwitter, FaYoutube } from 'react-icons/fa6';
import { IconType } from 'react-icons';

/* ------------------------------------------------------------------ */
/*  Types                                                             */
/* ------------------------------------------------------------------ */

interface ContactInfo {
  icon: IconType;
  label: string;
  value: string;
  href?: string;
}

interface SocialLink {
  icon: IconType;
  label: string;
  href: string;
  color: string;
}

interface InputFieldConfig {
  id: string;
  name: string;
  type: string;
  placeholder: string;
  colSpan?: boolean;
}

/* ------------------------------------------------------------------ */
/*  Constants                                                         */
/* ------------------------------------------------------------------ */

const CONTACT_INFO: ContactInfo[] = [
  {
    icon: FaLocationDot,
    label: 'Địa chỉ',
    value: '123 Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh',
  },
  {
    icon: FaPhone,
    label: 'Hotline',
    value: '1900 1234 56',
    href: 'tel:19001234',
  },
  {
    icon: FaEnvelope,
    label: 'Email',
    value: 'contact@example.com',
    href: 'mailto:contact@example.com',
  },
  {
    icon: FaClock,
    label: 'Giờ làm việc',
    value: 'Thứ 2 - Thứ 7: 8:00 - 18:00',
  },
];

const SOCIAL_LINKS: SocialLink[] = [
  { icon: FaFacebook, label: 'Facebook', href: '#', color: '#1877F2' },
  { icon: FaInstagram, label: 'Instagram', href: '#', color: '#E4405F' },
  { icon: FaXTwitter, label: 'Twitter', href: '#', color: '#000000' },
  { icon: FaYoutube, label: 'YouTube', href: '#', color: '#FF0000' },
];

const FORM_FIELDS: InputFieldConfig[] = [
  { id: 'contact-name', name: 'fullName', type: 'text', placeholder: 'Họ và tên *' },
  { id: 'contact-email', name: 'email', type: 'email', placeholder: 'Email *' },
  { id: 'contact-phone', name: 'phone', type: 'tel', placeholder: 'Số điện thoại' },
  { id: 'contact-subject', name: 'subject', type: 'text', placeholder: 'Chủ đề *' },
];

const MAP_EMBED_URL =
  'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.394812808498!2d106.70142937587844!3d10.78034468936553!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f4b3330bcc7%3A0x4db964d18e36e0e7!2zMTIzIE5ndXnhu4VuIEh14buHLCBC4bq_biBOZ2jDqSwgUXXhuq1uIDEsIEjhu5MgQ2jDrSBNaW5oLCBWaeG7h3QgTmFt!5e0!3m2!1svi!2s!4v1700000000000!5m2!1svi!2s';

/* ------------------------------------------------------------------ */
/*  Sub-components                                                    */
/* ------------------------------------------------------------------ */

const ContactInfoCard = ({ info }: { info: ContactInfo[] }) => (
  <div className="bg-white rounded-[16px] shadow-[0px_2px_12px_rgba(0,0,0,0.08)] p-[24px] sm:p-[32px]">
    <h2 className="m-0 font-bold text-[22px] sm:text-[24px] text-[var(--color-primary)] mb-[24px]">
      Thông Tin Liên Hệ
    </h2>
    <div className="flex flex-col gap-[20px]">
      {info.map(({ icon: Icon, label, value, href }) => (
        <div key={label} className="flex items-start gap-[14px]">
          <div className="w-[44px] h-[44px] rounded-[10px] bg-[var(--color-primary)]/10 inline-flex items-center justify-center shrink-0">
            <Icon className="text-[18px] text-[var(--color-primary)]" />
          </div>
          <div>
            <p className="m-0 font-medium text-[13px] text-[#A6A6A6] mb-[2px]">{label}</p>
            {href ? (
              <a
                href={href}
                className="font-medium text-[15px] text-[#25282B] hover:text-[var(--color-primary)] transition-colors"
              >
                {value}
              </a>
            ) : (
              <p className="m-0 font-medium text-[15px] text-[#25282B]">{value}</p>
            )}
          </div>
        </div>
      ))}
    </div>

    {/* Social links */}
    <div className="mt-[24px] pt-[20px] border-t border-[#ECECEC]">
      <p className="m-0 font-medium text-[14px] text-[#52575C] mb-[12px]">Theo dõi chúng tôi</p>
      <div className="flex gap-[10px]">
        {SOCIAL_LINKS.map(({ icon: Icon, label, href, color }) => (
          <a
            key={label}
            href={href}
            target="_blank"
            rel="noreferrer"
            aria-label={label}
            className="w-[40px] h-[40px] rounded-full border border-[#E0E0E0] inline-flex items-center justify-center text-[16px] text-[#52575C] hover:text-white transition-all duration-300"
            onMouseEnter={(e) => {
              e.currentTarget.style.backgroundColor = color;
              e.currentTarget.style.borderColor = color;
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.backgroundColor = '';
              e.currentTarget.style.borderColor = '';
            }}
          >
            <Icon />
          </a>
        ))}
      </div>
    </div>
  </div>
);

const ContactForm = ({
  fields,
  onSubmit,
}: {
  fields: InputFieldConfig[];
  onSubmit: (e: React.FormEvent<HTMLFormElement>) => void;
}) => (
  <div className="bg-white rounded-[16px] shadow-[0px_2px_12px_rgba(0,0,0,0.08)] p-[24px] sm:p-[32px]">
    <h2 className="m-0 font-bold text-[22px] sm:text-[24px] text-[var(--color-primary)] mb-[8px]">
      Gửi Tin Nhắn
    </h2>
    <p className="m-0 font-normal text-[14px] text-[#747578] mb-[24px]">
      Hãy để lại thông tin, chúng tôi sẽ liên hệ lại sớm nhất có thể.
    </p>

    <form onSubmit={onSubmit}>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-[14px]">
        {fields.map(({ id, name, type, placeholder }) => (
          <div key={id}>
            <input
              id={id}
              name={name}
              type={type}
              placeholder={placeholder}
              aria-label={placeholder.replace(' *', '')}
              className="w-full h-[50px] px-[18px] border border-[#E0E0E0] rounded-[8px] outline-none font-normal text-[14px] text-[#333] placeholder-[#A6A6A6] focus:border-[var(--color-primary)] transition-colors"
            />
          </div>
        ))}
      </div>
      <div className="mt-[14px]">
        <textarea
          name="message"
          placeholder="Nội dung tin nhắn *"
          aria-label="Nội dung tin nhắn"
          className="w-full h-[140px] p-[14px_18px] border border-[#E0E0E0] rounded-[8px] outline-none font-normal text-[14px] text-[#333] placeholder-[#A6A6A6] resize-y focus:border-[var(--color-primary)] transition-colors"
        />
      </div>
      <div className="mt-[20px]">
        <button
          type="submit"
          className="w-full sm:w-auto px-[40px] h-[48px] bg-[var(--color-primary)] rounded-[8px] border-0 font-semibold text-[16px] text-white cursor-pointer hover:brightness-110 transition-all"
        >
          Gửi Tin Nhắn
        </button>
      </div>
    </form>
  </div>
);

const MapSection = ({ embedUrl }: { embedUrl: string }) => (
  <div className="rounded-[16px] overflow-hidden shadow-[0px_2px_12px_rgba(0,0,0,0.08)]">
    <iframe
      src={embedUrl}
      width="100%"
      height="400"
      style={{ border: 0 }}
      allowFullScreen
      loading="lazy"
      referrerPolicy="no-referrer-when-downgrade"
      title="Bản đồ vị trí"
    />
  </div>
);

/* ------------------------------------------------------------------ */
/*  Main component                                                    */
/* ------------------------------------------------------------------ */

export const ContactContent = () => {
  const handleSubmit = useCallback((e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    // TODO: Implement contact form submission
  }, []);

  return (
    <section className="contact-page py-[45px] sm:py-[60px]">
      <div className="container mx-auto px-4">
        {/* Info + Form */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-[24px] sm:gap-[30px] mb-[30px] sm:mb-[40px]">
          <ContactInfoCard info={CONTACT_INFO} />
          <ContactForm fields={FORM_FIELDS} onSubmit={handleSubmit} />
        </div>

        {/* Map */}
        <MapSection embedUrl={MAP_EMBED_URL} />
      </div>
    </section>
  );
};
