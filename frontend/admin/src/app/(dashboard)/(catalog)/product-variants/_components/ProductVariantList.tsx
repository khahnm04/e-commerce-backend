"use client";
import React, { useMemo } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useListPage } from "@/hooks/useListPage";
import {
  PRODUCT_VARIANT_TABLE_HEADERS,
  PRODUCT_VARIANT_STATUS_OPTIONS,
  PRODUCT_VARIANT_ACTION_OPTIONS,
} from "@/constants/catalog/product-variant.constants";
import { TableControl } from "@/components/common/TableControl";
import { productVariantService } from "@/services/catalog/productVariant.service";
import { ProductVariantResponse } from "@/types/catalog/product-variant.types";
import { ProductVariantTableRow } from "./ProductVariantTableRow";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

export default function ProductVariantList({ isTrash = false }: { isTrash?: boolean }) {
  const {
    searchTerm,
    handleSearchChange,
    actionFilter,
    setActionFilter,
    filters,
    setFilter,
    data,
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
    handleStatusChange,
  } = useListPage<ProductVariantResponse, Parameters<typeof productVariantService.getAll>[0]>({
    service: productVariantService,
    isTrash,
    entityName: "biến thể sản phẩm",
    bulkActionKey: "productVariants",
  });

  const filterList: FilterConfig[] = useMemo(
    () => [
      {
        type: "select",
        id: "status",
        value: filters.status ?? "",
        onChange: (val: string | number) => setFilter("status", val),
        options: PRODUCT_VARIANT_STATUS_OPTIONS,
        placeholder: "Trạng thái",
      },
      { type: "date", id: "date-range", className: "!w-auto" },
    ],
    [filters, setFilter],
  );

  if (loading && isFirstLoad) return <LoadingSpinner />;

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/product-variants/create"
        trashLink="/product-variants/trash"
        searchTerm={searchTerm}
        onSearchChange={handleSearchChange}
        filterList={filterList}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={PRODUCT_VARIANT_ACTION_OPTIONS}
      />

      <DataTable
        headers={PRODUCT_VARIANT_TABLE_HEADERS}
        isSelectAll={isSelectAll}
        onSelectAll={handleSelectAll}
      >
        {data.map((item, index) => (
          <ProductVariantTableRow
            key={item.id}
            item={item}
            index={index}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onStatusChange={handleStatusChange}
            isSelected={isSelected(item.id)}
            onSelect={() => handleSelectItem(item.id)}
          />
        ))}
      </DataTable>

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title="Xác nhận xóa?"
        message={`Bạn có chắc chắn muốn xóa biến thể "${selectedItem?.name}" không?`}
      />
    </>
  );
}
