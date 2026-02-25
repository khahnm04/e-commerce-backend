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
  PRODUCT_TABLE_HEADERS,
  PRODUCT_STATUS_OPTIONS,
  PRODUCT_ACTION_OPTIONS,
  PRODUCT_TRASH_ACTION_OPTIONS,
} from "@/constants/catalog/product.constants";
import { TableControl } from "@/components/common/TableControl";
import { productService } from "@/services/catalog/product.service";
import { ProductResponse } from "@/types/catalog/product.types";
import { ProductTableRow } from "./ProductTableRow";

export default function ProductList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal,
  } = useTableManager<ProductResponse>();

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
      ? productService.getAllDeleted({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any)
      : productService.getAll({
        keyword: debouncedSearch || undefined,
        status: (statusFilter as string) || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
        page,
        size: pageSize,
      } as any),
    [isTrash, debouncedSearch, statusFilter, startDate, endDate, page, pageSize]
  );

  const { data: productData, meta, loading, reload } = useApiData<ProductResponse>({ fetchFn });

  useEffect(() => {
    if (!loading) isFirstLoad.current = false;
  }, [loading]);

  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () => {
    setSelectedIds(
      selectedIds.length === productData.length && productData.length > 0
        ? []
        : productData.map(i => i.id)
    );
  };

  const handleSelectItem = (id: string | number) => {
    setSelectedIds(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await productService.softDelete(selectedItem.id);
      toast.success("Xóa sản phẩm thành công!");
      reload();
    }
    handleCloseModal();
  };

  const handleRestore = async (id: number) => {
    try {
      await productService.restore(id);
      toast.success("Khôi phục sản phẩm thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra khi khôi phục!");
    }
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    try {
      await productService.updateStatus(id, { status: newStatus });
      toast.success("Cập nhật trạng thái thành công!");
      reload();
    } catch (error: unknown) {
      const err = error as Error;
      toast.error(err?.message || "Có lỗi xảy ra!");
    }
  };

  const handleApplyAction = async () => {
    if (!actionFilter || selectedIds.length === 0) {
      toast.error("Vui lòng chọn ít nhất 1 sản phẩm và hành động!");
      return;
    }
    try {
      await productService.bulkAction({
        products: selectedIds.map(id => Number(id)),
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
      options: PRODUCT_STATUS_OPTIONS,
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
        createLink="/products/create"
        trashLink="/products/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={isTrash ? PRODUCT_TRASH_ACTION_OPTIONS : PRODUCT_ACTION_OPTIONS}
        filterList={filterList}
        onClearFilter={handleClearFilter}
        onApplyAction={handleApplyAction}
      />

      <DataTable
        headers={PRODUCT_TABLE_HEADERS}
        isSelectAll={productData.length > 0 && selectedIds.length === productData.length}
        onSelectAll={handleSelectAll}
      >
        {productData.map((item) => (
          <ProductTableRow
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
          ? `Bạn có chắc chắn muốn xóa vĩnh viễn sản phẩm "${selectedItem?.name}" không?`
          : `Bạn có chắc chắn muốn đưa sản phẩm "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
