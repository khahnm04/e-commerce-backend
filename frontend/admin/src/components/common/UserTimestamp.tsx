// src/components/common/table/UserTimestamp.tsx
import React from "react";

interface UserTimestampProps {
  user?: string;
  date?: string;
}

export const UserTimestamp = ({ user, date }: UserTimestampProps) => {
  return (
    <div className="flex flex-col">
      <span className="font-semibold text-[14px] text-[var(--color-text)]">
        {user || "N/A"}
      </span>
      <span className="text-[12px] text-gray-500 mt-[2px]">
        {date}
      </span>
    </div>
  );
};
