"use client";
import React, { useState, useCallback, useEffect, useRef } from "react";
import { toast } from "react-hot-toast";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
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

export default function EmployeeList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<EmployeeResponse>();

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [debouncedSearch, setDebouncedSearch] = useState(searchTerm);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [branchFilter, setBranchFilter] = useState<string | number>("");
  const [branchOptions, setBranchOptions] = useState<{ id: string | number; name: string }[]>([{ id: "", name: "Tất cả" }]);
  const isFirstLoad = useRef(true);

  // Fetch branch options
  useEffect(() => {
    const fetchBranches = async () => {
      try {
        const res = await branchService.getAll();
        const branches = (res.data ?? []).map(b => ({ id: String(b.id), name: b.name }));
        setBranchOptions([{ id: "", name: "Tất cả" }, ...branches]);
      } catch {
        // ignore
      }
    };
    fetchBranches();
  }, []);

  // Debounce search term
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchTerm);
    }, 500);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const fetchFn = useCallback(
    () => isTrash
      ? employeeService.getAllDeleted({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        branchId: branchFilter ? Number(branchFilter) : undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as Parameters<typeof employeeService.getAllDeleted>[0])
      : employeeService.getAll({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        branchId: branchFilter ? Number(branchFilter) : undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as Parameters<typeof employeeService.getAll>[0]),
    [isTrash, debouncedSearch, statusFilter, branchFilter, startDate, endDate, page, pageSize]
  );

  const { data: employeeData, meta, loading, reload } = useApiData<EmployeeResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(
      selectedIds.length === employeeData.length && employeeData.length > 0
        ? []
        : employeeData.map(i => i.id)
    );
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await employeeService.softDelete(selectedItem.id);
      reload();
    }
    handleCloseModal();
  };

  const handleRestore = async (id: number) => {
    try {
      await employeeService.restore(id);
      toast.success("Khôi phục nhân viên thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra khi khôi phục!");
    }
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    try {
      await employeeService.updateStatus(id, {
        status: newStatus as EmployeeResponse["status"],
      });
      toast.success("Cập nhật trạng thái thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra!");
    }
  };

  const handleApplyAction = async () => {
    if (!actionFilter || selectedIds.length === 0) {
      toast.error("Vui lòng chọn ít nhất 1 nhân viên và hành động!");
      return;
    }
    try {
      await employeeService.bulkAction({
        employees: selectedIds.map(id => Number(id)),
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

  const handleStatusFilterChange = (value: string | number) => {
    setStatusFilter(value);
    setPage(1);
  };

  const handleBranchFilterChange = (value: string | number) => {
    setBranchFilter(value);
    setPage(1);
  };

  const handleClearFilter = () => {
    setStatusFilter("");
    setBranchFilter("");
    setStartDate("");
    setEndDate("");
    setPage(1);
  };

  const filterList: FilterConfig[] = [
    {
      type: "select",
      id: "status",
      value: statusFilter,
      onChange: handleStatusFilterChange,
      options: EMPLOYEE_STATUS_OPTIONS,
      placeholder: "Trạng thái"
    },
    {
      type: "select",
      id: "branch",
      value: branchFilter,
      onChange: handleBranchFilterChange,
      options: branchOptions,
      placeholder: "Chi nhánh"
    },
    {
      type: "date",
      id: "date-range",
      className: "!w-auto",
      startValue: startDate,
      endValue: endDate,
      onStartChange: (val: string) => { setStartDate(val); setPage(1); },
      onEndChange: (val: string) => { setEndDate(val); setPage(1); },
    },
  ];

  if (loading && isFirstLoad.current) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;

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
        isSelectAll={employeeData.length > 0 && selectedIds.length === employeeData.length}
        onSelectAll={handleSelectAll}
      >
        {employeeData.map((item) => (
          <EmployeeTableRow
            key={item.id}
            item={item}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onRestoreClick={() => handleRestore(item.id)}
            onStatusChange={handleStatusChange}
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
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhân viên "${selectedItem?.fullName}" không?`
          : `Bạn có chắc chắn muốn đưa nhân viên "${selectedItem?.fullName}" vào thùng rác không?`
        }
      />
    </>
  );
}
