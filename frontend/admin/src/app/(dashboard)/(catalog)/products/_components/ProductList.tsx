"use client";
import React, { useCallback, useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { Pagination } from "@/components/common/Pagination";
import { useListPage } from "@/hooks/useListPage";
import { useSelectOptions } from "@/hooks/useSelectOptions";
import { TableControl } from "@/components/common/TableControl";
import { productService } from "@/services/catalog/product.service";
import { brandService } from "@/services/catalog/brand.service";
import { categoryService } from "@/services/catalog/category.service";
import { ProductResponse } from "@/types/catalog/product.types";
import { ProductTableRow } from "./ProductTableRow";
import { BrandResponse } from "@/types/catalog/brand.types";
import { CategoryResponse } from "@/types/catalog/category.types";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import {
  PRODUCT_TABLE_HEADERS,
  PRODUCT_STATUS_OPTIONS,
  PRODUCT_ACTION_OPTIONS,
  PRODUCT_TRASH_ACTION_OPTIONS,
} from "@/constants/catalog/product.constants";

const mapToOption = (item: { id: number; name: string }) => ({
  id: String(item.id),
  name: item.name,
});

export default function ProductList({ isTrash = false }: { isTrash?: boolean }) {
  const brandOptions = useSelectOptions<BrandResponse>({
    fetchFn: brandService.getAll,
    mapFn: mapToOption,
  });

  const categoryOptions = useSelectOptions<CategoryResponse>({
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
      brandId: filters.brandId ? Number(filters.brandId) : undefined,
      categoryId: filters.categoryId ? Number(filters.categoryId) : undefined,
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
  } = useListPage<ProductResponse, Parameters<typeof productService.getAll>[0]>({
    service: productService,
    isTrash,
    entityName: "sản phẩm",
    bulkActionKey: "products",
    buildFilterParams,
  });

  const filterList: FilterConfig[] = useMemo(
    () => [
      {
        type: "select",
        id: "status",
        value: filters.status ?? "",
        onChange: (val: string | number) => setFilter("status", val),
        options: PRODUCT_STATUS_OPTIONS,
        placeholder: "Trạng thái",
      },
      {
        type: "select",
        id: "brandId",
        value: filters.brandId ?? "",
        onChange: (val: string | number) => setFilter("brandId", val),
        options: brandOptions,
        placeholder: "Nhãn hàng",
      },
      {
        type: "select",
        id: "categoryId",
        value: filters.categoryId ?? "",
        onChange: (val: string | number) => setFilter("categoryId", val),
        options: categoryOptions,
        placeholder: "Danh mục",
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
    [filters, brandOptions, categoryOptions, dateRange, setFilter, setStartDate, setEndDate],
  );

  if (loading && isFirstLoad) return <LoadingSpinner />;

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
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item) => (
          <ProductTableRow
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
            ? `Bạn có chắc chắn muốn xóa vĩnh viễn sản phẩm "${selectedItem?.name}" không?`
            : `Bạn có chắc chắn muốn đưa sản phẩm "${selectedItem?.name}" vào thùng rác không?`
        }
      />
    </>
  );
}
