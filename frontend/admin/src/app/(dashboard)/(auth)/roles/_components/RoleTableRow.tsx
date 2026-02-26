"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { RoleResponse } from "@/types/auth/role.types";
import { formatDateTime } from "@/lib/format-date";

interface RoleTableRowProps {
  item: RoleResponse;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onRestoreClick?: () => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const RoleTableRowInner = ({
  item,
  isTrash = false,
  onDeleteClick,
  onRestoreClick,
  isSelected,
  onSelect,
}: RoleTableRowProps) => {
  return (
    <tr>
      <TableCell>
        <TableCheckbox
          checked={isSelected}
          onChange={onSelect}
        />
      </TableCell>

      <TableCell>
        {item.code}
      </TableCell>

      <TableCell>
        {item.name}
      </TableCell>

      <TableCell className="max-w-[300px] truncate text-center">
        {item.description}
      </TableCell>

      <TableCell>
        <UserTimestamp
          user={item.createdBy}
          date={formatDateTime(item.createdAt)}
        />
      </TableCell>

      <TableCell>
        <UserTimestamp
          user={item.updatedBy}
          date={formatDateTime(item.updatedAt)}
        />
      </TableCell>

      <TableCell>
        {isTrash ? (
          <ActionGroup
            isTrash={true}
            viewLink={`/roles/trash/${item.id}`}
            hideDelete={true}
            onRestore={onRestoreClick}
          />
        ) : (
          <ActionGroup
            editLink={`/roles/${item.id}`}
            onDelete={onDeleteClick || (() => { })}
          />
        )}
      </TableCell>
    </tr>
  );
};

export const RoleTableRow = React.memo(RoleTableRowInner);
RoleTableRowInner.displayName = "RoleTableRow";
