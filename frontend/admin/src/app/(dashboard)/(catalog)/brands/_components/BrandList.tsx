"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { BrandTableRow } from "./BrandTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { BRAND_TABLE_HEADERS, BRAND_STATUS_OPTIONS, BRAND_ACTION_OPTIONS } from "@/constants/catalog/brand.constants";
import { TableControl } from "@/components/common/TableControl";
import { brandService } from "@/services/catalog/brand.service";
import { BrandResponse } from "@/types/catalog/brand.types";

interface BrandListProps { isTrash?: boolean; }

export default function BrandList({ isTrash = false }: BrandListProps) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<BrandResponse>();

  const fetchFn = useCallback(
    () => isTrash ? brandService.getAllDeleted({ keyword: searchTerm, status: statusFilter as any || undefined }) : brandService.getAll({ keyword: searchTerm, status: statusFilter as any || undefined }),
    [isTrash, searchTerm, statusFilter]
  );
  const { data: brandData, loading, reload } = useApiData<BrandResponse>({ fetchFn });

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(selectedIds.length === brandData.length && brandData.length > 0 ? [] : brandData.map(i => i.id));
  };
  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  };

  const filterList: FilterConfig[] = [
    { type: "select", id: "status", value: statusFilter, onChange: setStatusFilter, options: BRAND_STATUS_OPTIONS, placeholder: "Trạng thái" },
    { type: "date", id: "date-range", className: "!w-auto" },
  ];

  const handleConfirmDelete = async () => {
    if (selectedItem) { await brandService.softDelete(selectedItem.id); reload(); }
    handleCloseModal();
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    await brandService.updateStatus(id, { status: newStatus });
    reload();
  };

  if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl isTrash={isTrash} createLink="/brands/create" trashLink="/brands/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={BRAND_ACTION_OPTIONS} />
      <DataTable headers={BRAND_TABLE_HEADERS} isSelectAll={brandData.length > 0 && selectedIds.length === brandData.length} onSelectAll={handleSelectAll}>
        {brandData.map((item, index) => (
          <BrandTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} onStatusChange={handleStatusChange} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />
        ))}
      </DataTable>
      <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận đưa vào thùng rác?" message={`Bạn có chắc chắn muốn đưa nhãn hàng "${selectedItem?.name}" vào thùng rác không?`} />
    </>
  );
}
