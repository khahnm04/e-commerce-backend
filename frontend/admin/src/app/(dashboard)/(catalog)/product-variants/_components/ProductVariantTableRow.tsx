"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { PRODUCT_VARIANT_STATUS_OPTIONS } from "@/constants/catalog/product-variant.constants";
import { ProductVariantResponse } from "@/types/catalog/product-variant.types";
import { getStatusColorClass, formatCurrency } from "@/lib/format-utils";

interface Props {
  item: ProductVariantResponse;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const ProductVariantTableRow = ({
  item,
  isTrash = false,
  onDeleteClick,
  onStatusChange,
  isSelected,
  onSelect,
}: Props) => {
  const statusOptions = PRODUCT_VARIANT_STATUS_OPTIONS.filter((o) => o.id !== "");
  const formattedPrice = formatCurrency(item.price);

  return (
    <tr>
      <TableCell>
        <TableCheckbox checked={isSelected} onChange={onSelect} />
      </TableCell>

      <TableCell>{item.name}</TableCell>
      <TableCell>{item.sku}</TableCell>
      <TableCell>{item.productName}</TableCell>
      <TableCell>{formattedPrice}</TableCell>

      <TableCell>
        <div className="flex justify-center items-center ml-[20px]">
          <CustomSelect
            classNameCustom={getStatusColorClass(item.status)}
            value={item.status}
            options={statusOptions}
            onChange={(v) => onStatusChange?.(item.id, v as string)}
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
          editLink={`/product-variants/${item.id}`}
          onDelete={onDeleteClick || (() => {})}
          onRestore={() => {}}
        />
      </td>
    </tr>
  );
};
