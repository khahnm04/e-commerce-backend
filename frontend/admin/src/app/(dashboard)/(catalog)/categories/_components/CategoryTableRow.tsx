"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { CATEGORY_STATUS_OPTIONS } from "@/constants/catalog/category.constants";
import { CategoryResponse } from "@/types/catalog/category.types";
import { getStatusColorClass } from "@/lib/format-utils";

interface CategoryTableRowProps {
  item: CategoryResponse;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onRestoreClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const CategoryTableRow = ({
  item,
  isTrash = false,
  onDeleteClick,
  onRestoreClick,
  onStatusChange,
  isSelected,
  onSelect,
}: CategoryTableRowProps) => {
  const updateStatusOptions = CATEGORY_STATUS_OPTIONS.filter((opt) => opt.id !== "");

  return (
    <tr>
      <TableCell>
        <TableCheckbox checked={isSelected} onChange={onSelect} />
      </TableCell>

      <TableCell>{item.name}</TableCell>

      <TableCell>
        <TableImage src={item.iconUrl} alt={item.name} />
      </TableCell>

      <TableCell>{item.parentName || "—"}</TableCell>

      <TableCell>{item.position ?? 0}</TableCell>

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
        <UserTimestamp user={String(item.createdBy)} date={item.createdAt} />
      </TableCell>

      <TableCell>
        <UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} />
      </TableCell>

      <td
        className="py-[8px] px-[15px] text-center border-b
          border-[rgba(151,151,151,0.6)] text-[14px] font-[600]
          text-[var(--color-text)]"
      >
        <ActionGroup
          isTrash={isTrash}
          editLink={isTrash ? undefined : `/categories/${item.slug || item.id}`}
          viewLink={isTrash ? `/categories/trash/${item.id}` : undefined}
          onDelete={onDeleteClick || (() => {})}
          onRestore={onRestoreClick || (() => {})}
          hideDelete={isTrash}
        />
      </td>
    </tr>
  );
};
