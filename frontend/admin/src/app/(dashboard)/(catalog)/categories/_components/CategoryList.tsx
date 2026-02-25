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
  CATEGORY_TABLE_HEADERS,
  CATEGORY_STATUS_OPTIONS,
  CATEGORY_ACTION_OPTIONS,
  CATEGORY_TRASH_ACTION_OPTIONS,
} from "@/constants/catalog/category.constants";
import { TableControl } from "@/components/common/TableControl";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import { CategoryTableRow } from "./CategoryTableRow";

export default function CategoryList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<CategoryResponse>();

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [debouncedSearch, setDebouncedSearch] = useState(searchTerm);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [parentFilter, setParentFilter] = useState<string | number>("");
  const [parentOptions, setParentOptions] = useState<{ id: string | number; name: string }[]>([]);
  const isFirstLoad = useRef(true);

  // Debounce search term
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchTerm);
    }, 500);
    return () => clearTimeout(timer);
  }, [searchTerm]);

  // Fetch parent category options for filter
  useEffect(() => {
    const fetchParents = async () => {
      try {
        const res = await categoryService.getAll({ size: 99 });
        const categories = (res.data ?? []).map(c => ({ id: String(c.id), name: c.name }));
        setParentOptions([{ id: "", name: "Tất cả" }, ...categories]);
      } catch {
        // ignore
      }
    };
    fetchParents();
  }, []);

  const fetchFn = useCallback(
    () => isTrash
      ? categoryService.getAllDeleted({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        parentId: parentFilter ? Number(parentFilter) : undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any)
      : categoryService.getAll({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        parentId: parentFilter ? Number(parentFilter) : undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any),
    [isTrash, debouncedSearch, statusFilter, parentFilter, startDate, endDate, page, pageSize]
  );

  const { data: categoryData, meta, loading, reload } = useApiData<CategoryResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(
      selectedIds.length === categoryData.length && categoryData.length > 0
        ? []
        : categoryData.map(i => i.id)
    );
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await categoryService.softDelete(selectedItem.id);
      toast.success("Xóa danh mục thành công!");
      reload();
    }
    handleCloseModal();
  };

  const handleRestore = async (id: number) => {
    try {
      await categoryService.restore(id);
      toast.success("Khôi phục danh mục thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra khi khôi phục!");
    }
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    try {
      await categoryService.updateStatus(id, { status: newStatus });
      toast.success("Cập nhật trạng thái thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra!");
    }
  };

  const handleApplyAction = async () => {
    if (!actionFilter || selectedIds.length === 0) {
      toast.error("Vui lòng chọn ít nhất 1 danh mục và hành động!");
      return;
    }
    try {
      await categoryService.bulkAction({
        categories: selectedIds.map(id => Number(id)),
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
    setParentFilter("");
    setStartDate("");
    setEndDate("");
    setPage(1);
  };

  const handleParentFilterChange = (value: string | number) => {
    setParentFilter(value);
    setPage(1);
  };

  const filterList: FilterConfig[] = [
    {
      type: "select",
      id: "status",
      value: statusFilter,
      onChange: handleStatusFilterChange,
      options: CATEGORY_STATUS_OPTIONS,
      placeholder: "Trạng thái"
    },
    {
      type: "select",
      id: "parentId",
      value: parentFilter,
      onChange: handleParentFilterChange,
      options: parentOptions,
      placeholder: "Danh mục cha"
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
        isSelectAll={categoryData.length > 0 && selectedIds.length === categoryData.length}
        onSelectAll={handleSelectAll}
      >
        {categoryData.map((item) => (
          <CategoryTableRow
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
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn danh mục "${selectedItem?.name}" không?`
          : `Bạn có chắc chắn muốn đưa danh mục "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
