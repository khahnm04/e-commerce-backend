// eslint-disable-next-line @typescript-eslint/no-explicit-any
"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { CustomerTableRow } from "./CustomerTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import {
  CUSTOMER_TABLE_HEADERS,
  CUSTOMER_STATUS_OPTIONS,
  CUSTOMER_ACTION_OPTIONS,
  GENDER_OPTIONS
} from "@/constants/user/customer.constants";
import { TableControl } from "@/components/common/TableControl";
import { customerService } from "@/services/user/customer.service";
import { CustomerResponse } from "@/types/user/customer.types";

export default function CustomerList({
  isTrash = false
}: {
  isTrash?: boolean
}) {
  const {
    searchTerm,
    setSearchTerm,
    actionFilter,
    setActionFilter,
    statusFilter,
    setStatusFilter,
    creatorFilter,
    setCreatorFilter,
    isModalOpen,
    selectedItem,
    handleOpenDeleteModal,
    handleCloseModal
  } = useTableManager<CustomerResponse>();

  const fetchFn = useCallback(() =>
    isTrash
      ? customerService.getAllDeleted({
        keyword: searchTerm,
        status: statusFilter as any || undefined
      })
      : customerService.getAll({
        keyword: searchTerm,
        status: statusFilter as any || undefined
      }),
    [isTrash, searchTerm, statusFilter]);

  const { data, loading, reload } = useApiData<CustomerResponse>({ fetchFn });
  const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);

  const handleSelectAll = () =>
    setSelectedIds(selectedIds.length === data.length && data.length > 0
      ? []
      : data.map(i => i.id));

  const handleSelectItem = (id: string | number) =>
    setSelectedIds(prev => prev.includes(id)
      ? prev.filter(x => x !== id)
      : [...prev, id]);

  const filterList: FilterConfig[] = [
    {
      type: "select",
      id: "status",
      value: statusFilter,
      onChange: setStatusFilter,
      options: CUSTOMER_STATUS_OPTIONS,
      placeholder: "Trạng thái"
    },
    {
      type: "select",
      id: "gender",
      value: creatorFilter,
      onChange: setCreatorFilter,
      options: GENDER_OPTIONS,
      placeholder: "Giới tính"
    },
    {
      type: "date",
      id: "date-range",
      className: "!w-auto"
    },
  ];

  const handleConfirmDelete = async () => {
    if (selectedItem) {
      await customerService.softDelete(selectedItem.id);
      reload();
    }
    handleCloseModal();
  };

  const handleStatusChange = async (id: string | number, newStatus: string) => {
    await customerService.updateStatus(id, {
      status: newStatus as CustomerResponse["status"]
    }); reload();
  };

  if (loading) {
    return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
  }

  return (
    <>
      <TableControl
        isTrash={isTrash}
        createLink="/customers/create"
        trashLink="/customers/trash"
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        filterList={filterList}
        actionFilter={actionFilter}
        onActionFilterChange={setActionFilter}
        actionOptions={CUSTOMER_ACTION_OPTIONS}
      />

      <DataTable
        headers={CUSTOMER_TABLE_HEADERS}
        isSelectAll={data.length > 0 && selectedIds.length === data.length}
        onSelectAll={handleSelectAll}
      >
        {data.map((item, index) => (
          <CustomerTableRow
            key={item.id}
            item={item}
            index={index}
            isTrash={isTrash}
            onDeleteClick={() => handleOpenDeleteModal(item)}
            onStatusChange={handleStatusChange}
            isSelected={selectedIds.includes(item.id)}
            onSelect={() => handleSelectItem(item.id)}
          />
        ))}
      </DataTable>

      <ConfirmModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        onConfirm={handleConfirmDelete}
        title="Xác nhận đưa vào thùng rác?"
        message={`Bạn có chắc chắn muốn đưa khách hàng "${selectedItem?.fullName}" vào thùng rác không?`}
      />
    </>
  );
}
