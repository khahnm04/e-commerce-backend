"use client";
import React from "react";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useListPage } from "@/hooks/useListPage";
import {
  VARIANT_VALUE_TABLE_HEADERS,
  VARIANT_VALUE_ACTION_OPTIONS,
} from "@/constants/catalog/variant-value.constants";
import { TableControl } from "@/components/common/TableControl";
import { variantValueService } from "@/services/catalog/variantValue.service";
import { VariantValueResponse } from "@/types/catalog/variant-value.types";
import { VariantValueTableRow } from "./VariantValueTableRow";

export default function VariantValueList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm,
    handleSearchChange,
    actionFilter,
    setActionFilter,
    data,
    loading,
    isFirstLoad,
    isSelected,
    isSelectAll,
    handleSelectAll,
    handleSelectItem,
    isModalOpen,
    selectedItem,
    handleOpenDeleteModal,
    handleCloseModal,
    handleConfirmDelete,
  } = useListPage<VariantValueResponse, Parameters<typeof variantValueService.getAll>[0]>({
    service: variantValueService,
    isTrash,
    entityName: "giá trị biến thể",
    bulkActionKey: "variantValues",
  });

  if (loading && isFirstLoad)
    return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/variant-values/create"
        trashLink="/variant-values/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        filterList={[{ type: "date", id: "date-range", className: "!w-auto" }]}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={VARIANT_VALUE_ACTION_OPTIONS}
      />
      <DataTable
        headers={VARIANT_VALUE_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item, index) => (
          <VariantValueTableRow
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
        message={`Bạn có chắc chắn muốn xóa giá trị "${selectedItem?.value}" không?`}
      />
    </>
  );
}
