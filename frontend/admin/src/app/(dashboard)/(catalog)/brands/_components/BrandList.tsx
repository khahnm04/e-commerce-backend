/* eslint-disable @typescript-eslint/no-explicit-any */
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
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<BrandResponse>();

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
      ? brandService.getAllDeleted({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any)
      : brandService.getAll({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any),
    [isTrash, debouncedSearch, statusFilter, startDate, endDate, page, pageSize]
  );

  const { data: brandData, meta, loading, reload } = useApiData<BrandResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(
      selectedIds.length === brandData.length && brandData.length > 0
        ? []
        : brandData.map(i => i.id)
    );
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await brandService.softDelete(selectedItem.id);
      toast.success("Xóa nhãn hàng thành công!");
      reload();
    }
    handleCloseModal();
  };

  const handleRestore = async (id: number) => {
    try {
      await brandService.restore(id);
      toast.success("Khôi phục nhãn hàng thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra khi khôi phục!");
    }
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    try {
      await brandService.updateStatus(id, { status: newStatus });
      toast.success("Cập nhật trạng thái thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra!");
    }
  };

  const handleApplyAction = async () => {
    if (!actionFilter || selectedIds.length === 0) {
      toast.error("Vui lòng chọn ít nhất 1 nhãn hàng và hành động!");
      return;
    }
    try {
      await brandService.bulkAction({
        brands: selectedIds.map(id => Number(id)),
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

  const handleClearFilter = () => {
    setStatusFilter("");
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
      options: BRAND_STATUS_OPTIONS,
      placeholder: "Trạng thái"
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
        isSelectAll={brandData.length > 0 && selectedIds.length === brandData.length}
        onSelectAll={handleSelectAll}
      >
        {brandData.map((item) => (
          <BrandTableRow
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
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn nhãn hàng "${selectedItem?.name}" không?`
          : `Bạn có chắc chắn muốn đưa nhãn hàng "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
