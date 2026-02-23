"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { VariantTableRow } from "./VariantTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { VARIANT_TABLE_HEADERS, VARIANT_ACTION_OPTIONS } from "@/constants/variant.constants";
import { TableControl } from "@/components/common/TableControl";
import { variantService } from "@/services/variant.service";
import { VariantResponse } from "@/types/variant.types";

export default function VariantList({ isTrash = false }: { isTrash?: boolean }) {
    const { searchTerm, setSearchTerm, actionFilter, setActionFilter, isModalOpen, selectedItem, handleOpenDeleteModal, handleCloseModal } = useTableManager<VariantResponse>();
    const fetchFn = useCallback(() => isTrash ? variantService.getAllDeleted({ keyword: searchTerm }) : variantService.getAll({ keyword: searchTerm }), [isTrash, searchTerm]);
    const { data, loading, reload } = useApiData<VariantResponse>({ fetchFn });
    const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);
    const handleSelectAll = () => setSelectedIds(selectedIds.length === data.length && data.length > 0 ? [] : data.map(i => i.id));
    const handleSelectItem = (id: string | number) => setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
    const filterList: FilterConfig[] = [{ type: "date", id: "date-range", className: "!w-auto" }];
    const handleConfirmDelete = async () => { if (selectedItem) { await variantService.softDelete(selectedItem.id); reload(); } handleCloseModal(); };

    if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
    return (
        <>
            <TableControl isTrash={isTrash} createLink="/variants/create" trashLink="/variants/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={VARIANT_ACTION_OPTIONS} />
            <DataTable headers={VARIANT_TABLE_HEADERS} isSelectAll={data.length > 0 && selectedIds.length === data.length} onSelectAll={handleSelectAll}>
                {data.map((item, index) => (<VariantTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />))}
            </DataTable>
            <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận xóa?" message={`Bạn có chắc chắn muốn xóa biến thể "${selectedItem?.name}" không?`} />
        </>
    );
}
