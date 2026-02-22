/* eslint-disable @next/next/no-img-element */
"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { STATUS_OPTIONS } from "@/constants/category.constants";

interface CategoryTableRowProps {
  item: any;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const CategoryTableRow = ({
  item,
  index,
  isTrash = false,
  onDeleteClick,
  onStatusChange,
  isSelected,
  onSelect
}: CategoryTableRowProps) => {

  const updateStatusOptions = STATUS_OPTIONS.filter(opt => opt.id !== "");

  const getStatusColorClass = (status: string) => {
    switch (status) {
      case "Hoạt động":
        return "text-[#10B981]";
      case "Tạm dừng":
        return "text-[#F87171]";
      default:
        return "";
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
          {item.id}
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
            user={item.createdBy}
            date={item.createdAt}
          />
        </TableCell>

        <TableCell>
          <UserTimestamp
            user={item.updatedBy}
            date={item.updatedAt}
          />
        </TableCell>

        <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
          <ActionGroup
            isTrash={isTrash}
            editLink={`/categories/${item.slug}`}
            onDelete={onDeleteClick || (() => { })}
            onRestore={() => console.log("Restore Category", item.id)}
          />
        </td>
      </tr>
    </>
  );
};
