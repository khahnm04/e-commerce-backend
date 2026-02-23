"use client";
import React, { useState, useCallback } from "react";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { ROLE_TABLE_HEADERS, ROLE_ACTION_OPTIONS } from "@/constants/role.constants";
import { TableControl } from "@/components/common/TableControl";
import { roleService } from "@/services/role.service";
import { RoleResponse } from "@/types/role.types";
import { RoleTableRow } from "./RoleTableRow";

export default function RoleList() {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<RoleResponse>();

  const fetchFn = useCallback(() => roleService.getAll(), []);
  const { data: roleData, loading, reload } = useApiData<RoleResponse>({ fetchFn });

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(selectedIds.length === roleData.length && roleData.length > 0 ? [] : roleData.map(i => i.id));
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await roleService.delete(selectedItem.id);
      reload();
    }
    handleCloseModal();
  };

  // filter locally because API doesn't support search yet inside getAll
  const filteredData = roleData.filter(role =>
    role.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    role.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl
        createLink="/roles/create"
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={ROLE_ACTION_OPTIONS}
        filterList={[]} // No filters as requested
      />
      <DataTable
        headers={ROLE_TABLE_HEADERS}
        isSelectAll={filteredData.length > 0 && selectedIds.length === filteredData.length}
        onSelectAll={handleSelectAll}
      >
        {filteredData.map((item) => (
          <RoleTableRow
            key={item.id}
            item={item}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            isSelected={selectedIds.includes(item.id)}
            onSelect={() => handleSelectItem(item.id)}
          />
        ))}
      </DataTable>
      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title="Xác nhận xóa?"
        message={`Bạn có chắc chắn muốn xóa nhóm quyền "${selectedItem?.name}" không?`}
      />
    </>
  );
}
