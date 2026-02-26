"use client";
import React, { useCallback, useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import { useSelectOptions } from "@/hooks/useSelectOptions";
import {
  CATEGORY_TABLE_HEADERS,
  CATEGORY_STATUS_OPTIONS,
  CATEGORY_ACTION_OPTIONS,
  CATEGORY_TRASH_ACTION_OPTIONS,
} from "@/constants/catalog/category.constants";
import { TableControl } from "@/components/common/TableControl";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import { CategoryTableRow } from "./CategoryTableRow";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

const mapToOption = (item: { id: number; name: string }) => ({
  id: String(item.id),
  name: item.name,
});

export default function CategoryList({ isTrash = false }: { isTrash?: boolean }) {
  const parentOptions = useSelectOptions<CategoryResponse>({
    fetchFn: categoryService.getAll,
    mapFn: mapToOption,
  });

  const buildFilterParams = useCallback(
    ({
      keyword,
      page,
      size,
      filters,
      dateRange,
    }: Parameters<NonNullable<Parameters<typeof useListPage>[0]["buildFilterParams"]>>[0]) => ({
      keyword,
      page,
      size,
      status: (filters.status as string) || undefined,
      parentId: filters.parentId ? Number(filters.parentId) : undefined,
      startDate: dateRange.startDate || undefined,
      endDate: dateRange.endDate || undefined,
    }),
    [],
  );

  const {
    searchTerm,
    handleSearchChange,
    setPage,
    handlePageSizeChange,
    filters,
    setFilter,
    dateRange,
    setStartDate,
    setEndDate,
    handleClearFilter,
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
    handleStatusChange,
    handleApplyAction,
  } = useListPage<CategoryResponse, Parameters<typeof categoryService.getAll>[0]>({
    service: categoryService,
    isTrash,
    entityName: "danh mục",
    bulkActionKey: "categories",
    buildFilterParams,
  });

  const filterList: FilterConfig[] = useMemo(
    () => [
      {
        type: "select",
        id: "status",
        value: filters.status ?? "",
        onChange: (val: string | number) => setFilter("status", val),
        options: CATEGORY_STATUS_OPTIONS,
        placeholder: "Trạng thái",
      },
      {
        type: "select",
        id: "parentId",
        value: filters.parentId ?? "",
        onChange: (val: string | number) => setFilter("parentId", val),
        options: parentOptions,
        placeholder: "Danh mục cha",
      },
      {
        type: "date",
        id: "date-range",
        className: "!w-auto",
        startValue: dateRange.startDate,
        endValue: dateRange.endDate,
        onStartChange: setStartDate,
        onEndChange: setEndDate,
      },
    ],
    [filters, parentOptions, dateRange, setFilter, setStartDate, setEndDate],
  );

  if (loading && isFirstLoad) return <LoadingSpinner />;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/categories/create"
        trashLink="/categories/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? CATEGORY_TRASH_ACTION_OPTIONS : CATEGORY_ACTION_OPTIONS}
        filterList={filterList}
        onClearFilter={handleClearFilter}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={CATEGORY_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <CategoryTableRow
            key={item.id}
            item={item}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onRestoreClick={() => handleRestore(item.id)}
            onStatusChange={handleStatusChange}
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
            ? `Bạn có chắc chắn muốn xóa vĩnh viễn danh mục "${selectedItem?.name}" không?`
            : `Bạn có chắc chắn muốn đưa danh mục "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
