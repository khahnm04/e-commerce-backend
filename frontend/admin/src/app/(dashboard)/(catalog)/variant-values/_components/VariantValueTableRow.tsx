"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { VariantValueResponse } from "@/types/catalog/variant-value.types";

interface Props {
  item: VariantValueResponse;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const VariantValueTableRowInner = ({
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

    <TableCell>{item.value}</TableCell>
    <TableCell>{item.code}</TableCell>
    <TableCell>{item.variantName}</TableCell>
    <TableCell>{item.displayOrder}</TableCell>

    <TableCell>
      <UserTimestamp user={String(item.createdBy)} date={item.createdAt} />
    </TableCell>

    <TableCell>
      <UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} />
    </TableCell>

    <TableCell>
      <ActionGroup
        isTrash={isTrash}
        editLink={`/variant-values/${item.id}`}
        onDelete={onDeleteClick || (() => {})}
        onRestore={() => {}}
      />
    </TableCell>
  </tr>
);

export const VariantValueTableRow = React.memo(VariantValueTableRowInner);
VariantValueTableRowInner.displayName = "VariantValueTableRow";
