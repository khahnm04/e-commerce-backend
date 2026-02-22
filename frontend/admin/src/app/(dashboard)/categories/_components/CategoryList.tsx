"use client"
import React, { useEffect, useState } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { CategoryTableRow } from "./CategoryTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import {
  CATEGORY_TABLE_HEADERS,
  STATUS_OPTIONS,
  ACTION_OPTIONS,
  CREATOR_OPTIONS
} from "@/constants/category.constants";
import { MOCK_CATEGORY_LIST } from "@/data/mock-category";
import { TableControl } from "@/components/common/TableControl";

interface CategoryListProps {
  isTrash?: boolean;
}

export default function CategoryList({ isTrash = false }: CategoryListProps) {
  const [categoryData, setCategoryData] = useState<any[]>([]);
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
    setCategoryData(MOCK_CATEGORY_LIST);
  }, [isTrash]);

  // Handle Selection
  const handleSelectAll = () => {
    if (selectedIds.length === categoryData.length && categoryData.length > 0) {
      setSelectedIds([]);
    } else {
      setSelectedIds(categoryData.map(item => item.id));
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
      options: STATUS_OPTIONS,
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
    {
      type: "date",
      id: "date-range",
      className: "!w-auto",
    },
  ];

  const handleConfirmDelete = () => {
    console.log("Xóa Category:", selectedItem?.name);
    handleCloseModal();
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    console.log(`Đang gọi API đổi status của ID ${id} thành: ${newStatus}`);
    // await categoryService.updateStatus(id, newStatus);
    // Sau khi xong thì load lại data
  };

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/categories/create"
        trashLink="/categories/trash"
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        filterList={filterList}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={ACTION_OPTIONS}
      />

      <DataTable
        headers={CATEGORY_TABLE_HEADERS}
        isSelectAll={categoryData.length > 0 && selectedIds.length === categoryData.length}
        onSelectAll={handleSelectAll}
      >
        {categoryData && (
          categoryData.map((item, index) => (
            <CategoryTableRow
              key={item.id}
              item={item}
              index={index}
              isTrash={isTrash}
              onDeleteClick={() => handleOpenDeleteModal(item)}
              onStatusChange={handleStatusChange}
              isSelected={selectedIds.includes(item.id)}
              onSelect={() => handleSelectItem(item.id)}
            />
          ))
        )}
      </DataTable>

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title="Xác nhận đưa vào thùng rác?"
        message={`Bạn có chắc chắn muốn đưa danh mục "${selectedItem?.name}" vào thùng rác không?`}
      />
    </>
  );
}
