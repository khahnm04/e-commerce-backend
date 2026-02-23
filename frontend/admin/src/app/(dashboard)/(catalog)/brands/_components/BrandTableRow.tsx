"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { BRAND_STATUS_OPTIONS } from "@/constants/brand.constants";
import { BrandResponse } from "@/types/brand.types";

interface Props {
  item: BrandResponse;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const BrandTableRow = ({ item, isTrash = false, onDeleteClick, onStatusChange, isSelected, onSelect }: Props) => {
  const statusOptions = BRAND_STATUS_OPTIONS.filter(o => o.id !== "");
  const statusColor = item.status === "ACTIVE" ? "text-[#10B981]" : "text-[#F59E0B]";

  return (
    <tr>
      <TableCell><TableCheckbox checked={isSelected} onChange={onSelect} /></TableCell>
      <TableCell>{item.name}</TableCell>
      <TableCell><TableImage src={item.logoUrl} alt={item.name} /></TableCell>
      <TableCell>{item.slug}</TableCell>
      <TableCell>{item.country}</TableCell>
      <TableCell>
        <div className="flex justify-center items-center ml-[20px]">
          <CustomSelect classNameCustom={statusColor} value={item.status} options={statusOptions} onChange={(v) => onStatusChange?.(item.id, v as string)} placeholder={item.status} />
        </div>
      </TableCell>
      <TableCell><UserTimestamp user={String(item.createdBy)} date={item.createdAt} /></TableCell>
      <TableCell><UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} /></TableCell>
      <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
        <ActionGroup isTrash={isTrash} editLink={`/brands/${item.slug || item.id}`} onDelete={onDeleteClick || (() => { })} onRestore={() => { }} />
      </td>
    </tr>
  );
};
