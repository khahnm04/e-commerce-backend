"use client";
import React, { useState, useCallback, useEffect, useRef } from "react";
import { toast } from "react-hot-toast";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { ROLE_TABLE_HEADERS, ROLE_ACTION_OPTIONS, ROLE_TRASH_ACTION_OPTIONS } from "@/constants/auth/role.constants";
import { TableControl } from "@/components/common/TableControl";
import { roleService } from "@/services/auth/role.service";
import { RoleResponse } from "@/types/auth/role.types";
import { RoleTableRow } from "./RoleTableRow";

export default function RoleList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<RoleResponse>();

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [debouncedSearch, setDebouncedSearch] = useState(searchTerm);
  const isFirstLoad = useRef(true);

  // Debounce search term
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchTerm);
      setPage(1);
    }, 500);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const fetchFn = useCallback(
    () => isTrash
      ? roleService.getAllDeleted({ keyword: debouncedSearch || undefined, page, size: pageSize })
      : roleService.getAll({ keyword: debouncedSearch || undefined, page, size: pageSize }),
    [isTrash, debouncedSearch, page, pageSize]
  );
  const { data: roleData, meta, loading, reload } = useApiData<RoleResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

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

  const handleRestore = async (id: number) => {
    await roleService.restore(id);
    reload();
  };

  const handleApplyAction = async () => {
    if (!actionFilter || selectedIds.length === 0) {
      toast.error("Vui lòng chọn ít nhất 1 nhóm quyền và hành động!");
      return;
    }
    try {
      await roleService.bulkAction({
        ids: selectedIds.map(id => Number(id)),
        action: String(actionFilter),
      });
      toast.success("Thực hiện hành động thành công!");
      setSelectedIds([]);
      setActionFilter("");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra!");
    }
  };

  const handleSearchChange = (val: string) => {
    setSearchTerm(val);
    setPage(1);
  };

  const handlePageSizeChange = (size: number) => {
    setPageSize(size);
    setPage(1);
  };

  if (loading && isFirstLoad.current) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

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
        isSelectAll={roleData.length > 0 && selectedIds.length === roleData.length}
        onSelectAll={handleSelectAll}
      >
        {roleData.map((item) => (
          <RoleTableRow
            key={item.id}
            item={item}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onRestoreClick={() => handleRestore(item.id)}
            isSelected={selectedIds.includes(item.id)}
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
        message={isTrash
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhóm quyền "${selectedItem?.name}" không?`
          : `Bạn có chắc chắn muốn đưa nhóm quyền "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
