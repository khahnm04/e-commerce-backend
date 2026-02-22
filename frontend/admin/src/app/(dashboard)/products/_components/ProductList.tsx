"use client"
import React, { useEffect, useState } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { ProductTableRow } from "./ProductTableRow";
import { useTableManager } from "@/hooks/useTableManager";
import {
  PRODUCT_TABLE_HEADERS,
  PRODUCT_STATUS_OPTIONS,
  PRODUCT_ACTION_OPTIONS,
  CREATOR_OPTIONS
} from "@/constants/product.constants";
import { MOCK_PRODUCT_LIST } from "@/data/mock-product";
import { TableControl } from "@/components/common/TableControl";

interface ProductListProps {
  isTrash?: boolean;
}

export default function ProductList({ isTrash = false }: ProductListProps) {
  const [productData, setProductData] = useState<any[]>([]);
  const {
    searchTerm, setSearchTerm,
    actionFilter, setActionFilter,
    statusFilter, setStatusFilter,
    creatorFilter, setCreatorFilter,
    isModalOpen, selectedItem,
    handleOpenDeleteModal, handleCloseModal
  } = useTableManager<any>();

  // Selection State
  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  useEffect(() => {
    // Giả sử lấy dữ liệu mẫu hoặc gọi API
    setProductData(MOCK_PRODUCT_LIST);
  }, [isTrash]);

  // Handle Selection
  const handleSelectAll = () => {
    if (selectedIds.length === productData.length && productData.length > 0) {
      setSelectedIds([]);
    } else {
      setSelectedIds(productData.map(item => item.id));
    }
  };

  const handleSelectItem = (id: string | number) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(prev => prev.filter(itemId => itemId !== id));
    } else {
      setSelectedIds(prev => [...prev, id]);
    }
  };

  const filterList: FilterConfig[] = [
    {
      type: "select",
      id: "status",
      value: statusFilter,
      onChange: setStatusFilter,
      options: PRODUCT_STATUS_OPTIONS,
      placeholder: "Trạng thái",
    },
    {
      type: "select",
      id: "creator",
      value: creatorFilter,
      onChange: setCreatorFilter,
      options: CREATOR_OPTIONS,
      placeholder: "Người tạo",
    },
    { type: "date", id: "date-range", className: "!w-auto" },
  ];

  const handleConfirmDelete = () => {
    handleCloseModal();
  };

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/products/create"
        trashLink="/products/trash"
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        filterList={filterList}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={PRODUCT_ACTION_OPTIONS}
        onApplyAction={() => console.log("Áp dụng hành động:", actionFilter)}
      />

      <DataTable
        headers={PRODUCT_TABLE_HEADERS}
        isSelectAll={productData.length > 0 && selectedIds.length === productData.length}
        onSelectAll={handleSelectAll}
      >
        {productData.map((item, index) => (
          <ProductTableRow
            key={item.id}
            item={item}
            index={index}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            isSelected={selectedIds.includes(item.id)}
            onSelect={() => handleSelectItem(item.id)}
          />
        ))}
      </DataTable>

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title={isTrash ? "Xác nhận xóa vĩnh viễn?" : "Xác nhận đưa vào thùng rác?"}
        message={`Bạn có chắc chắn muốn xóa sản phẩm "${selectedItem?.name}" không?`}
      />
    </>
  );
}
