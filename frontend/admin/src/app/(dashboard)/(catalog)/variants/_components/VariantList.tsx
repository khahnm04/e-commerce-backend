"use client";
import React from "react";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useListPage } from "@/hooks/useListPage";
import { VARIANT_TABLE_HEADERS, VARIANT_ACTION_OPTIONS } from "@/constants/catalog/variant.constants";
import { TableControl } from "@/components/common/TableControl";
import { variantService } from "@/services/catalog/variant.service";
import { VariantResponse } from "@/types/catalog/variant.types";
import { VariantTableRow } from "./VariantTableRow";

export default function VariantList({ isTrash = false }: { isTrash?: boolean }) {
    const {
        searchTerm, handleSearchChange,
        actionFilter, setActionFilter,
        data, loading, isFirstLoad,
        isSelected, isSelectAll,
        handleSelectAll, handleSelectItem,
        isModalOpen, selectedItem,
        handleOpenDeleteModal, handleCloseModal,
        handleConfirmDelete,
    } = useListPage<VariantResponse, Parameters<typeof variantService.getAll>[0]>({
        service: variantService,
        isTrash,
        entityName: "biến thể",
        bulkActionKey: "variants",
    });

    if (loading && isFirstLoad) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

    return (
        <>
            <TableControl
                isTrash={isTrash}
                createLink="/variants/create"
                trashLink="/variants/trash"
                searchTerm={searchTerm}
                onSearchChange={handleSearchChange}
                filterList={[{ type: "date", id: "date-range", className: "!w-auto" }]}
                actionFilter={actionFilter}
                onActionFilterChange={setActionFilter}
                actionOptions={VARIANT_ACTION_OPTIONS}
            />
            <DataTable
                headers={VARIANT_TABLE_HEADERS}
                isSelectAll={isSelectAll}
                onSelectAll={handleSelectAll}
            >
                {data.map((item, index) => (
                    <VariantTableRow
                        key={item.id}
                        item={item}
                        index={index}
                        isTrash={isTrash}
                        onDeleteClick={() => handleOpenDeleteModal(item)}
                        isSelected={isSelected(item.id)}
                        onSelect={() => handleSelectItem(item.id)}
                    />
                ))}
            </DataTable>
            <ConfirmModal
                isOpen={isModalOpen}
                onClose={handleCloseModal}
                onConfirm={handleConfirmDelete}
                title="Xác nhận xóa?"
                message={`Bạn có chắc chắn muốn xóa biến thể "${selectedItem?.name}" không?`}
            />
        </>
    );
}
