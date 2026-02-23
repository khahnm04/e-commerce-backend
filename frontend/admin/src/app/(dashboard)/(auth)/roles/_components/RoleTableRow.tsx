"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { RoleResponse } from "@/types/role.types";

interface RoleTableRowProps {
  item: RoleResponse;
  onDeleteClick?: () => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const RoleTableRow = ({
  item,
  onDeleteClick,
  isSelected,
  onSelect
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

      <TableCell className="max-w-[300px] truncate">
        {item.description}
      </TableCell>

      <TableCell>
        <UserTimestamp
          date={item.createdAt}
        />
      </TableCell>

      <TableCell>
        <UserTimestamp
          date={item.updatedAt}
        />
      </TableCell>

      <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
        <ActionGroup
          editLink={`/roles/${item.id}`}
          onDelete={onDeleteClick || (() => { })}
        />
      </td>
    </tr>
  );
};
