"use client";
import React from "react";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import { TableControl } from "@/components/common/TableControl";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";
import { RoleTableRow } from "./RoleTableRow";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import {
  ROLE_TABLE_HEADERS,
  ROLE_ACTION_OPTIONS,
  ROLE_TRASH_ACTION_OPTIONS,
} from "@/constants/auth/role.constants";

export default function RoleList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm,
    handleSearchChange,
    setPage,
    handlePageSizeChange,
    actionFilter,
    setActionFilter,
    data,
    meta,
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
    handleRestore,
    handleApplyAction,
  } = useListPage<RoleResponse, Parameters<typeof roleService.getAll>[0]>({
    service: roleService,
    isTrash,
    entityName: "nhóm quyền",
    bulkActionKey: "ids",
  });

  if (loading && isFirstLoad) return <LoadingSpinner />;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/roles/create"
        trashLink="/roles/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? ROLE_TRASH_ACTION_OPTIONS : ROLE_ACTION_OPTIONS}
        filterList={[]}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={ROLE_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <RoleTableRow
            key={item.id}
            item={item}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onRestoreClick={() => handleRestore(item.id)}
            isSelected={isSelected(item.id)}
            onSelect={() => handleSelectItem(item.id)}
          />
        ))}
      </DataTable>

      <Pagination
        meta={meta}
        onPageChange={setPage}
        onPageSizeChange={handlePageSizeChange}
      />

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title={isTrash ? "Xác nhận xóa vĩnh viễn?" : "Xác nhận đưa vào thùng rác?"}
        message={
          isTrash
            ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhóm quyền "${selectedItem?.name}" không?`
            : `Bạn có chắc chắn muốn đưa nhóm quyền "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
