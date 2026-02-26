"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { CUSTOMER_STATUS_OPTIONS } from "@/constants/user/customer.constants";
import { CustomerResponse } from "@/types/user/customer.types";
import { getStatusColorClass, getGenderLabel } from "@/lib/format-utils";

interface CustomerTableRowProps {
  item: CustomerResponse;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onRestoreClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const CustomerTableRowInner = ({
  item,
  isTrash = false,
  onDeleteClick,
  onRestoreClick,
  onStatusChange,
  isSelected,
  onSelect,
}: CustomerTableRowProps) => {
  const updateStatusOptions = CUSTOMER_STATUS_OPTIONS.filter((opt) => opt.id !== "");

  return (
    <tr>
      <TableCell>
        <TableCheckbox checked={isSelected} onChange={onSelect} />
      </TableCell>

      <TableCell>{item.fullName}</TableCell>

      <TableCell>
        <TableImage src={item.avatarUrl} alt={item.fullName} />
      </TableCell>

      <TableCell>{item.phoneNumber}</TableCell>

      <TableCell>{item.email}</TableCell>

      <TableCell>{getGenderLabel(item.gender)}</TableCell>

      <TableCell>
        {isTrash ? (
          <span className={`font-[600] ${getStatusColorClass(item.status)}`}>
            {updateStatusOptions.find((opt) => opt.id === item.status)?.name || item.status}
          </span>
        ) : (
          <div className="flex justify-center items-center ml-[20px]">
            <CustomSelect
              classNameCustom={getStatusColorClass(item.status)}
              value={item.status}
              options={updateStatusOptions}
              onChange={(newValue) => {
                if (onStatusChange) {
                  onStatusChange(item.id, newValue as string);
                }
              }}
              placeholder={item.status}
            />
          </div>
        )}
      </TableCell>

      <TableCell>
        <UserTimestamp user={String(item.createdBy)} date={item.createdAt} />
      </TableCell>

      <TableCell>
        <UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} />
      </TableCell>

      <TableCell>
        {isTrash ? (
          <ActionGroup
            isTrash={true}
            viewLink={`/customers/trash/${item.id}`}
            hideDelete={true}
            onRestore={onRestoreClick}
          />
        ) : (
          <ActionGroup editLink={`/customers/${item.id}`} onDelete={onDeleteClick || (() => {})} />
        )}
      </TableCell>
    </tr>
  );
};

export const CustomerTableRow = React.memo(CustomerTableRowInner);
CustomerTableRowInner.displayName = "CustomerTableRow";
