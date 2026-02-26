"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { VariantResponse } from "@/types/catalog/variant.types";

interface Props {
  item: VariantResponse;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const VariantTableRowInner = ({
  item,
  isTrash = false,
  onDeleteClick,
  isSelected,
  onSelect,
}: Props) => (
  <tr>
    <TableCell>
      <TableCheckbox checked={isSelected} onChange={onSelect} />
    </TableCell>

    <TableCell>{item.name}</TableCell>
    <TableCell>{item.code}</TableCell>

    <TableCell>
      <UserTimestamp user={String(item.createdBy)} date={item.createdAt} />
    </TableCell>

    <TableCell>
      <UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} />
    </TableCell>

    <TableCell>
      <ActionGroup
        isTrash={isTrash}
        editLink={`/variants/${item.id}`}
        onDelete={onDeleteClick || (() => {})}
        onRestore={() => {}}
      />
    </TableCell>
  </tr>
);

export const VariantTableRow = React.memo(VariantTableRowInner);
VariantTableRowInner.displayName = "VariantTableRow";
