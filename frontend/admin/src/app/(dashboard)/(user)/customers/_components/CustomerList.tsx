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
  CUSTOMER_TABLE_HEADERS,
  CUSTOMER_STATUS_OPTIONS,
  CUSTOMER_ACTION_OPTIONS,
  CUSTOMER_TRASH_ACTION_OPTIONS,
  GENDER_OPTIONS
} from "@/constants/user/customer.constants";
import { TableControl } from "@/components/common/TableControl";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";
import { CustomerTableRow } from "./CustomerTableRow";

export default function CustomerList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    creatorFilter: genderFilter, setCreatorFilter: setGenderFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<CustomerResponse>();

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [debouncedSearch, setDebouncedSearch] = useState(searchTerm);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const isFirstLoad = useRef(true);

  // Debounce search term
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchTerm);
    }, 500);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  const fetchFn = useCallback(
    () => isTrash
      ? customerService.getAllDeleted({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        gender: (genderFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as Parameters<typeof customerService.getAllDeleted>[0])
      : customerService.getAll({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        gender: (genderFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as Parameters<typeof customerService.getAll>[0]),
    [isTrash, debouncedSearch, statusFilter, genderFilter, startDate, endDate, page, pageSize]
  );

  const { data: customerData, meta, loading, reload } = useApiData<CustomerResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(
      selectedIds.length === customerData.length && customerData.length > 0
        ? []
        : customerData.map(i => i.id)
    );
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await customerService.softDelete(selectedItem.id);
      reload();
    }
    handleCloseModal();
  };

  const handleRestore = async (id: number) => {
    try {
      await customerService.restore(id);
      toast.success("Khôi phục khách hàng thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra khi khôi phục!");
    }
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    try {
      await customerService.updateStatus(id, {
        status: newStatus as CustomerResponse["status"],
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
      toast.error("Vui lòng chọn ít nhất 1 khách hàng và hành động!");
      return;
    }
    try {
      await customerService.bulkAction({
        customers: selectedIds.map(id => Number(id)),
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

  const handleGenderFilterChange = (value: string | number) => {
    setGenderFilter(value);
    setPage(1);
  };

  const handleClearFilter = () => {
    setStatusFilter("");
    setGenderFilter("");
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
      options: CUSTOMER_STATUS_OPTIONS,
      placeholder: "Trạng thái"
    },
    {
      type: "select",
      id: "gender",
      value: genderFilter,
      onChange: handleGenderFilterChange,
      options: GENDER_OPTIONS,
      placeholder: "Giới tính"
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
        isSelectAll={customerData.length > 0 && selectedIds.length === customerData.length}
        onSelectAll={handleSelectAll}
      >
        {customerData.map((item) => (
          <CustomerTableRow
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
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn khách hàng "${selectedItem?.fullName}" không?`
          : `Bạn có chắc chắn muốn đưa khách hàng "${selectedItem?.fullName}" vào thùng rác không?`
        }
      />
    </>
  );
}
