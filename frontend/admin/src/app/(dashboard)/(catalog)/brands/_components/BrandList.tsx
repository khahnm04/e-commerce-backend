"use client";
import React, { useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import {
  BRAND_TABLE_HEADERS,
  BRAND_STATUS_OPTIONS,
  BRAND_ACTION_OPTIONS,
  BRAND_TRASH_ACTION_OPTIONS,
} from "@/constants/catalog/brand.constants";
import { TableControl } from "@/components/common/TableControl";
import { brandService } from "@/services/catalog/brand.service";
import { BrandResponse } from "@/types/catalog/brand.types";
import { BrandTableRow } from "./BrandTableRow";

export default function BrandList({ isTrash = false }: { isTrash?: boolean }) {
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
  } = useListPage<BrandResponse, Parameters<typeof brandService.getAll>[0]>({
    service: brandService,
    isTrash,
    entityName: "nhãn hàng",
    bulkActionKey: "brands",
  });

  const filterList: FilterConfig[] = useMemo(
    () => [
      {
        type: "select",
        id: "status",
        value: filters.status ?? "",
        onChange: (val: string | number) => setFilter("status", val),
        options: BRAND_STATUS_OPTIONS,
        placeholder: "Trạng thái",
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
    [filters, dateRange, setFilter, setStartDate, setEndDate],
  );

  if (loading && isFirstLoad)
    return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/brands/create"
        trashLink="/brands/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? BRAND_TRASH_ACTION_OPTIONS : BRAND_ACTION_OPTIONS}
        filterList={filterList}
        onClearFilter={handleClearFilter}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={BRAND_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <BrandTableRow
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

      <Pagination meta={meta} onPageChange={setPage} onPageSizeChange={handlePageSizeChange} />

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title={isTrash ? "Xác nhận xóa vĩnh viễn?" : "Xác nhận đưa vào thùng rác?"}
        message={
          isTrash
            ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhãn hàng "${selectedItem?.name}" không?`
            : `Bạn có chắc chắn muốn đưa nhãn hàng "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
