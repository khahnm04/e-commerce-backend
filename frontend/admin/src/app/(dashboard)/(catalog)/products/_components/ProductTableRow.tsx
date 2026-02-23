"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { ProductResponse } from "@/types/product.types";

const STATUS_OPTIONS = [
  { id: "ACTIVE", name: "Hoạt động" },
  { id: "INACTIVE", name: "Tạm dừng" },
  { id: "OUT_OF_STOCK", name: "Hết hàng" },
  { id: "PENDING", name: "Chờ duyệt" },
  { id: "DISCONTINUED", name: "Ngừng kinh doanh" },
];

interface Props {
  item: ProductResponse;
  index: number;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

export const ProductTableRow = ({ item, isTrash = false, onDeleteClick, onStatusChange, isSelected, onSelect }: Props) => {
  const getStatusColor = (s: string) => {
    switch (s) {
      case "ACTIVE": return "text-[#10B981]";
      case "INACTIVE": return "text-[#F59E0B]";
      case "OUT_OF_STOCK": return "text-[#EF4444]";
      case "PENDING": return "text-[#3B82F6]";
      case "DISCONTINUED": return "text-[#6B7280]";
      default: return "";
    }
  };
  const formattedPrice = new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(item.price);

  return (
    <tr>
      <TableCell><TableCheckbox checked={isSelected} onChange={onSelect} /></TableCell>
      <TableCell>{item.name}</TableCell>
      <TableCell><TableImage src={item.thumbnail} alt={item.name} /></TableCell>
      <TableCell>{item.productCode || "—"}</TableCell>
      <TableCell>{formattedPrice}</TableCell>
      <TableCell>{item.brandName}</TableCell>
      <TableCell>
        <div className="flex justify-center items-center ml-[20px]">
          <CustomSelect classNameCustom={getStatusColor(item.status)} value={item.status} options={STATUS_OPTIONS} onChange={(v) => onStatusChange?.(item.id, v as string)} placeholder={item.status} />
        </div>
      </TableCell>
      <TableCell><UserTimestamp user={String(item.createdBy)} date={item.createdAt} /></TableCell>
      <TableCell><UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} /></TableCell>
      <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
        <ActionGroup isTrash={isTrash} editLink={`/products/${item.slug || item.id}`} onDelete={onDeleteClick || (() => { })} onRestore={() => { }} />
      </td>
    </tr>
  );
};
