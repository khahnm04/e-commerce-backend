"use client";
import React, { useCallback, useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import { useSelectOptions } from "@/hooks/useSelectOptions";
import {
  EMPLOYEE_TABLE_HEADERS,
  EMPLOYEE_STATUS_OPTIONS,
  EMPLOYEE_ACTION_OPTIONS,
  EMPLOYEE_TRASH_ACTION_OPTIONS,
} from "@/constants/user/employee.constants";
import { TableControl } from "@/components/common/TableControl";
import { employeeService } from "@/services/user/employee.service";
import { branchService } from "@/services/branch/branch.service";
import { EmployeeResponse } from "@/types/user/employee.types";
import { EmployeeTableRow } from "./EmployeeTableRow";

interface BranchSimple {
  id: number;
  name: string;
}

const mapBranchToOption = (item: BranchSimple) => ({
  id: String(item.id),
  name: item.name,
});

export default function EmployeeList({ isTrash = false }: { isTrash?: boolean }) {
  const branchOptions = useSelectOptions<BranchSimple>({
    fetchFn: branchService.getAll as (params: Record<string, unknown>) => ReturnType<typeof branchService.getAll>,
    mapFn: mapBranchToOption,
  });

  const buildFilterParams = useCallback(
    ({ keyword, page, size, filters, dateRange }: Parameters<NonNullable<Parameters<typeof useListPage>[0]["buildFilterParams"]>>[0]) => ({
      keyword,
      page,
      size,
      status: (filters.status as string) || undefined,
      branchId: filters.branchId ? Number(filters.branchId) : undefined,
      startDate: dateRange.startDate || undefined,
      endDate: dateRange.endDate || undefined,
    }),
    []
  );

  const {
    searchTerm, handleSearchChange,
    setPage, handlePageSizeChange,
    filters, setFilter, dateRange, setStartDate, setEndDate,
    handleClearFilter,
    actionFilter, setActionFilter,
    data, meta, loading, isFirstLoad,
    isSelected, isSelectAll,
    handleSelectAll, handleSelectItem,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
    handleConfirmDelete, handleRestore,
    handleStatusChange, handleApplyAction,
  } = useListPage<EmployeeResponse, Parameters<typeof employeeService.getAll>[0]>({
    service: employeeService,
    isTrash,
    entityName: "nhân viên",
    bulkActionKey: "employees",
    buildFilterParams,
  });

  const filterList: FilterConfig[] = useMemo(() => [
    {
      type: "select",
      id: "status",
      value: filters.status ?? "",
      onChange: (val: string | number) => setFilter("status", val),
      options: EMPLOYEE_STATUS_OPTIONS,
      placeholder: "Trạng thái"
    },
    {
      type: "select",
      id: "branch",
      value: filters.branchId ?? "",
      onChange: (val: string | number) => setFilter("branchId", val),
      options: branchOptions,
      placeholder: "Chi nhánh"
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
  ], [filters, branchOptions, dateRange, setFilter, setStartDate, setEndDate]);

  if (loading && isFirstLoad) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/employees/create"
        trashLink="/employees/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? EMPLOYEE_TRASH_ACTION_OPTIONS : EMPLOYEE_ACTION_OPTIONS}
        filterList={filterList}
        onClearFilter={handleClearFilter}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={EMPLOYEE_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <EmployeeTableRow
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
        message={isTrash
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhân viên "${selectedItem?.fullName}" không?`
          : `Bạn có chắc chắn muốn đưa nhân viên "${selectedItem?.fullName}" vào thùng rác không?`
        }
      />
    </>
  );
}
