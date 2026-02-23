"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ProductTableRow } from "./ProductTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { TableControl } from "@/components/common/TableControl";
import { productService } from "@/services/product.service";
import { ProductResponse } from "@/types/product.types";

const TABLE_HEADERS = ["Tên sản phẩm", "Ảnh", "Mã", "Giá", "Nhãn hàng", "Trạng thái", "Tạo bởi", "Cập nhật bởi", "Hành động"];
const STATUS_OPTIONS = [{ id: "", name: "Tất cả" }, { id: "ACTIVE", name: "Hoạt động" }, { id: "INACTIVE", name: "Tạm dừng" }, { id: "OUT_OF_STOCK", name: "Hết hàng" }, { id: "PENDING", name: "Chờ duyệt" }, { id: "DISCONTINUED", name: "Ngừng kinh doanh" }];
const ACTION_OPTIONS = [{ id: "ACTIVE", name: "Hoạt động" }, { id: "INACTIVE", name: "Tạm dừng" }, { id: "delete", name: "Xóa" }];

export default function ProductList({ isTrash = false }: { isTrash?: boolean }) {
  const { searchTerm, setSearchTerm, actionFilter, setActionFilter, statusFilter, setStatusFilter, isModalOpen, selectedItem, handleOpenDeleteModal, handleCloseModal } = useTableManager<ProductResponse>();
  const fetchFn = useCallback(() => isTrash ? productService.getAllDeleted({ keyword: searchTerm, status: statusFilter as any || undefined }) : productService.getAll({ keyword: searchTerm, status: statusFilter as any || undefined }), [isTrash, searchTerm, statusFilter]);
  const { data, loading, reload } = useApiData<ProductResponse>({ fetchFn });
  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);
  const handleSelectAll = () => setSelectedIds(selectedIds.length === data.length && data.length > 0 ? [] : data.map(i => i.id));
  const handleSelectItem = (id: string | number) => setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  const filterList: FilterConfig[] = [
    { type: "select", id: "status", value: statusFilter, onChange: setStatusFilter, options: STATUS_OPTIONS, placeholder: "Trạng thái" },
    { type: "date", id: "date-range", className: "!w-auto" },
  ];
  const handleConfirmDelete = async () => { if (selectedItem) { await productService.softDelete(selectedItem.id); reload(); } handleCloseModal(); };
  const handleStatusChange = async (id: string | number, newStatus: string) => { await productService.updateStatus(id, { status: newStatus }); reload(); };

  if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
  return (
    <>
      <TableControl isTrash={isTrash} createLink="/products/create" trashLink="/products/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={ACTION_OPTIONS} />
      <DataTable headers={TABLE_HEADERS} isSelectAll={data.length > 0 && selectedIds.length === data.length} onSelectAll={handleSelectAll}>
        {data.map((item, index) => (<ProductTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} onStatusChange={handleStatusChange} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />))}
      </DataTable>
      <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận đưa vào thùng rác?" message={`Bạn có chắc chắn muốn đưa sản phẩm "${selectedItem?.name}" vào thùng rác không?`} />
    </>
  );
}
