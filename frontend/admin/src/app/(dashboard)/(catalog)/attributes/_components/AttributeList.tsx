"use client";
import React from "react";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useListPage } from "@/hooks/useListPage";
import { ATTRIBUTE_TABLE_HEADERS, ATTRIBUTE_ACTION_OPTIONS } from "@/constants/catalog/attribute.constants";
import { TableControl } from "@/components/common/TableControl";
import { attributeService } from "@/services/catalog/attribute.service";
import { AttributeResponse } from "@/types/catalog/attribute.types";
import { AttributeTableRow } from "./AttributeTableRow";

export default function AttributeList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, handleSearchChange,
    actionFilter, setActionFilter,
    data, loading, isFirstLoad,
    isSelected, isSelectAll,
    handleSelectAll, handleSelectItem,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
    handleConfirmDelete,
  } = useListPage<AttributeResponse, Parameters<typeof attributeService.getAll>[0]>({
    service: attributeService,
    isTrash,
    entityName: "thuộc tính",
    bulkActionKey: "attributes",
  });

  if (loading && isFirstLoad) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/attributes/create"
        trashLink="/attributes/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        filterList={[{ type: "date", id: "date-range", className: "!w-auto" }]}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={ATTRIBUTE_ACTION_OPTIONS}
      />
      <DataTable
        headers={ATTRIBUTE_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item, index) => (
          <AttributeTableRow
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
        message={`Bạn có chắc chắn muốn xóa thuộc tính "${selectedItem?.name}" không?`}
      />
    </>
  );
}
