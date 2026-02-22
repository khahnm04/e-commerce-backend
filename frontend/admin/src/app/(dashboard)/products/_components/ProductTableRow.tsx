/* eslint-disable @next/next/no-img-element */
"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { CustomSelect } from "@/components/common/CustomSelect";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { PRODUCT_STATUS_OPTIONS } from "@/constants/product.constants";

interface ProductTableRowProps {
  item: any;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const ProductTableRow = ({
  item,
  index,
  isTrash = false,
  onDeleteClick,
  onStatusChange,
  isSelected,
  onSelect
}: ProductTableRowProps) => {

  const updateStatusOptions = PRODUCT_STATUS_OPTIONS.filter(opt => opt.id !== "");

  const getStatusColorClass = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return "text-green-600";
      case "INACTIVE":
        return "text-gray-500";
      case "OUT_OF_STOCK":
        return "text-orange-500";
      case "PENDING":
        return "text-blue-500";
      case "DISCONTINUED":
        return "text-red-500";
      default:
        return "text-[var(--color-text)]";
    }
  };

  return (
    <>
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
            src={item.thumbnail}
            alt={item.name}
          />
        </TableCell>

        <TableCell>
          {new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(item.price)}
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
          <div>{item.createdBy}</div>
          <div className="text-[12px] text-gray-500">{item.createdAt}</div>
        </TableCell>

        <TableCell>
          <div>{item.updatedBy}</div>
          <div className="text-[12px] text-gray-500">{item.updatedAt}</div>
        </TableCell>

        <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
          <ActionGroup
            isTrash={isTrash}
            editLink={`/products/${item.slug}`}
            onDelete={onDeleteClick || (() => { })}
            onRestore={() => console.log("Restore Product", item.id)}
          />
        </td>
      </tr>
    </>
  );
};
