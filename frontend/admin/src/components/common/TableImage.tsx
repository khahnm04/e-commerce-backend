/* eslint-disable @next/next/no-img-element */
import React from "react";

interface TableImageProps {
  src?: string;
  alt?: string;
}

export const TableImage = ({ src, alt = "Image" }: TableImageProps) => {
  return (
    <>
      <img
        className="w-[60px] h-[60px] rounded-[6px] object-cover mx-auto border border-gray-100"
        src={src || "/assets/images/placeholder.png"}
        alt={alt}
      />
    </>
  );
};
