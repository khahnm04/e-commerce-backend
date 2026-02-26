"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { BRAND_STATUS_OPTIONS } from "@/constants/catalog/brand.constants";
import { BrandResponse } from "@/types/catalog/brand.types";
import { getStatusColorClass } from "@/lib/format-utils";

interface BrandTableRowProps {
  item: BrandResponse;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onRestoreClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const BrandTableRowInner = ({
  item,
  isTrash = false,
  onDeleteClick,
  onRestoreClick,
  onStatusChange,
  isSelected,
  onSelect,
}: BrandTableRowProps) => {
  const updateStatusOptions = BRAND_STATUS_OPTIONS.filter((opt) => opt.id !== "");

  return (
    <tr>
      <TableCell>
        <TableCheckbox
          checked={isSelected}
          onChange={onSelect}
        />
      </TableCell>

      <TableCell>
        {item.name}
      </TableCell>

      <TableCell>
        <TableImage
          src={item.logoUrl}
          alt={item.name}
        />
      </TableCell>

      <TableCell>
        {item.slug}
      </TableCell>

      <TableCell>
        {item.country}
      </TableCell>

      <TableCell>
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
      </TableCell>

      <TableCell>
        <UserTimestamp
          user={String(item.createdBy)}
          date={item.createdAt}
        />
      </TableCell>

      <TableCell>
        <UserTimestamp
          user={String(item.updatedBy)}
          date={item.updatedAt}
        />
      </TableCell>

      <TableCell>
        <ActionGroup
          isTrash={isTrash}
          editLink={isTrash ? undefined : `/brands/${item.slug || item.id}`}
          viewLink={isTrash ? `/brands/trash/${item.id}` : undefined}
          onDelete={onDeleteClick || (() => { })}
          onRestore={onRestoreClick || (() => { })}
          hideDelete={isTrash}
        />
      </TableCell>
    </tr>
  );
};

export const BrandTableRow = React.memo(BrandTableRowInner);
BrandTableRowInner.displayName = "BrandTableRow";
