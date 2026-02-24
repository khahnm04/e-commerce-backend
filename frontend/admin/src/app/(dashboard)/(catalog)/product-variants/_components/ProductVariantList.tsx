"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ProductVariantTableRow } from "./ProductVariantTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { PRODUCT_VARIANT_TABLE_HEADERS, PRODUCT_VARIANT_STATUS_OPTIONS, PRODUCT_VARIANT_ACTION_OPTIONS } from "@/constants/catalog/product-variant.constants";
import { TableControl } from "@/components/common/TableControl";
import { productVariantService } from "@/services/catalog/productVariant.service";
import { ProductVariantResponse } from "@/types/catalog/product-variant.types";

export default function ProductVariantList({ isTrash = false }: { isTrash?: boolean }) {
    const { searchTerm, setSearchTerm, actionFilter, setActionFilter, statusFilter, setStatusFilter, isModalOpen, selectedItem, handleOpenDeleteModal, handleCloseModal } = useTableManager<ProductVariantResponse>();
    const fetchFn = useCallback(() => isTrash ? productVariantService.getAllDeleted({ keyword: searchTerm }) : productVariantService.getAll({ keyword: searchTerm }), [isTrash, searchTerm]);
    const { data, loading, reload } = useApiData<ProductVariantResponse>({ fetchFn });
    const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);
    const handleSelectAll = () => setSelectedIds(selectedIds.length === data.length && data.length > 0 ? [] : data.map(i => i.id));
    const handleSelectItem = (id: string | number) => setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
    const filterList: FilterConfig[] = [
        { type: "select", id: "status", value: statusFilter, onChange: setStatusFilter, options: PRODUCT_VARIANT_STATUS_OPTIONS, placeholder: "Trạng thái" },
        { type: "date", id: "date-range", className: "!w-auto" },
    ];
    const handleConfirmDelete = async () => { if (selectedItem) { await productVariantService.softDelete(selectedItem.id); reload(); } handleCloseModal(); };
    const handleStatusChange = async (id: string | number, newStatus: string) => { await productVariantService.updateStatus(id, { status: newStatus }); reload(); };

    if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
    return (
        <>
            <TableControl isTrash={isTrash} createLink="/product-variants/create" trashLink="/product-variants/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={PRODUCT_VARIANT_ACTION_OPTIONS} />
            <DataTable headers={PRODUCT_VARIANT_TABLE_HEADERS} isSelectAll={data.length > 0 && selectedIds.length === data.length} onSelectAll={handleSelectAll}>
                {data.map((item, index) => (<ProductVariantTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} onStatusChange={handleStatusChange} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />))}
            </DataTable>
            <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận xóa?" message={`Bạn có chắc chắn muốn xóa biến thể "${selectedItem?.name}" không?`} />
        </>
    );
}
