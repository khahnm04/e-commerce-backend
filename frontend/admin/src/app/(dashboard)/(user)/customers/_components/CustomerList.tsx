"use client";
import React, { useCallback, useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import {
  CUSTOMER_TABLE_HEADERS,
  CUSTOMER_STATUS_OPTIONS,
  CUSTOMER_ACTION_OPTIONS,
  CUSTOMER_TRASH_ACTION_OPTIONS,
  GENDER_OPTIONS,
} from "@/constants/user/customer.constants";
import { TableControl } from "@/components/common/TableControl";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import { CustomerTableRow } from "./CustomerTableRow";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

export default function CustomerList({ isTrash = false }: { isTrash?: boolean }) {
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
      gender: (filters.gender as string) || undefined,
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
  } = useListPage<CustomerResponse, Parameters<typeof customerService.getAll>[0]>({
    service: customerService,
    isTrash,
    entityName: "khách hàng",
    bulkActionKey: "customers",
    buildFilterParams,
  });

  const filterList: FilterConfig[] = useMemo(
    () => [
      {
        type: "select",
        id: "status",
        value: filters.status ?? "",
        onChange: (val: string | number) => setFilter("status", val),
        options: CUSTOMER_STATUS_OPTIONS,
        placeholder: "Trạng thái",
      },
      {
        type: "select",
        id: "gender",
        value: filters.gender ?? "",
        onChange: (val: string | number) => setFilter("gender", val),
        options: GENDER_OPTIONS,
        placeholder: "Giới tính",
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

  if (loading && isFirstLoad) return <LoadingSpinner />;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/customers/create"
        trashLink="/customers/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? CUSTOMER_TRASH_ACTION_OPTIONS : CUSTOMER_ACTION_OPTIONS}
        filterList={filterList}
        onClearFilter={handleClearFilter}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={CUSTOMER_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <CustomerTableRow
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
            ? `Bạn có chắc chắn muốn xóa vĩnh viễn khách hàng "${selectedItem?.fullName}" không?`
            : `Bạn có chắc chắn muốn đưa khách hàng "${selectedItem?.fullName}" vào thùng rác không?`
        }
      />
    </>
  );
}
