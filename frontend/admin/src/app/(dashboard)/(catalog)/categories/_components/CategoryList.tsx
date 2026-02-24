"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { CategoryTableRow } from "./CategoryTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { CATEGORY_TABLE_HEADERS, STATUS_OPTIONS, ACTION_OPTIONS } from "@/constants/catalog/category.constants";
import { TableControl } from "@/components/common/TableControl";
import { categoryService } from "@/services/catalog/category.service";
import { Category } from "@/types/catalog/category.types";

interface CategoryListProps { isTrash?: boolean; }

export default function CategoryList({ isTrash = false }: CategoryListProps) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<Category>();

  const fetchFn = useCallback(
    () => isTrash ? categoryService.getAllDeleted({ keyword: searchTerm, status: statusFilter as any || undefined }) : categoryService.getAll({ keyword: searchTerm, status: statusFilter as any || undefined }),
    [isTrash, searchTerm, statusFilter]
  );
  const { data: categoryData, loading, reload } = useApiData<Category>({ fetchFn });

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(selectedIds.length === categoryData.length && categoryData.length > 0 ? [] : categoryData.map(i => i.id));
  };
  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  };

  const filterList: FilterConfig[] = [
    { type: "select", id: "status", value: statusFilter, onChange: setStatusFilter, options: STATUS_OPTIONS, placeholder: "Trạng thái" },
    { type: "date", id: "date-range", className: "!w-auto" },
  ];

  const handleConfirmDelete = async () => {
    if (selectedItem) { await categoryService.softDelete(selectedItem.id); reload(); }
    handleCloseModal();
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    await categoryService.updateStatus(id, { status: newStatus });
    reload();
  };

  if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl isTrash={isTrash} createLink="/categories/create" trashLink="/categories/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={ACTION_OPTIONS} />
      <DataTable headers={CATEGORY_TABLE_HEADERS} isSelectAll={categoryData.length > 0 && selectedIds.length === categoryData.length} onSelectAll={handleSelectAll}>
        {categoryData.map((item, index) => (
          <CategoryTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} onStatusChange={handleStatusChange} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />
        ))}
      </DataTable>
      <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận đưa vào thùng rác?" message={`Bạn có chắc chắn muốn đưa danh mục "${selectedItem?.name}" vào thùng rác không?`} />
    </>
  );
}
