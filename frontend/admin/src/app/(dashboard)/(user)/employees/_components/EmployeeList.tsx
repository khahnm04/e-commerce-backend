"use client";
import React, { useState, useCallback } from "react";
import { FilterConfig } from "@/components/common/DynamicTableFilter";
import { DataTable } from "@/components/common/DataTable";
import { EmployeeTableRow } from "./EmployeeTableRow";
import { ConfirmModal } from "@/components/common/ConfirmModal";
import { useTableManager } from "@/hooks/useTableManager";
import { useApiData } from "@/hooks/useApiData";
import { EMPLOYEE_TABLE_HEADERS, EMPLOYEE_STATUS_OPTIONS, EMPLOYEE_ACTION_OPTIONS } from "@/constants/employee.constants";
import { TableControl } from "@/components/common/TableControl";
import { employeeService } from "@/services/employee.service";
import { EmployeeResponse } from "@/types/employee.types";

export default function EmployeeList({ isTrash = false }: { isTrash?: boolean }) {
    const { searchTerm, setSearchTerm, actionFilter, setActionFilter, statusFilter, setStatusFilter, isModalOpen, selectedItem, handleOpenDeleteModal, handleCloseModal } = useTableManager<EmployeeResponse>();
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const fetchFn = useCallback(() => isTrash ? employeeService.getAllDeleted({ keyword: searchTerm, status: statusFilter as any || undefined }) : employeeService.getAll({ keyword: searchTerm, status: statusFilter as any || undefined }), [isTrash, searchTerm, statusFilter]);
    const { data, loading, reload } = useApiData<EmployeeResponse>({ fetchFn });
    const [selectedIds, setSelectedIds] = useState<(string | number)[]>([]);
    const handleSelectAll = () => setSelectedIds(selectedIds.length === data.length && data.length > 0 ? [] : data.map(i => i.id));
    const handleSelectItem = (id: string | number) => setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);
    const filterList: FilterConfig[] = [
        { type: "select", id: "status", value: statusFilter, onChange: setStatusFilter, options: EMPLOYEE_STATUS_OPTIONS, placeholder: "Trạng thái" },
        { type: "date", id: "date-range", className: "!w-auto" },
    ];
    const handleConfirmDelete = async () => { if (selectedItem) { await employeeService.softDelete(selectedItem.id); reload(); } handleCloseModal(); };
    const handleStatusChange = async (id: string | number, newStatus: string) => { await employeeService.updateStatus(id, { status: newStatus as EmployeeResponse["status"] }); reload(); };

    if (loading) return <div className="text-center py-10 text-[var(--color-text)]">Đang tải dữ liệu...</div>;
    return (
        <>
            <TableControl isTrash={isTrash} createLink="/employees/create" trashLink="/employees/trash" searchTerm={searchTerm} onSearchChange={setSearchTerm} filterList={filterList} actionFilter={actionFilter} onActionFilterChange={setActionFilter} actionOptions={EMPLOYEE_ACTION_OPTIONS} />
            <DataTable headers={EMPLOYEE_TABLE_HEADERS} isSelectAll={data.length > 0 && selectedIds.length === data.length} onSelectAll={handleSelectAll}>
                {data.map((item, index) => (<EmployeeTableRow key={item.id} item={item} index={index} isTrash={isTrash} onDeleteClick={() => handleOpenDeleteModal(item)} onStatusChange={handleStatusChange} isSelected={selectedIds.includes(item.id)} onSelect={() => handleSelectItem(item.id)} />))}
            </DataTable>
            <ConfirmModal isOpen={isModalOpen} onClose={handleCloseModal} onConfirm={handleConfirmDelete} title="Xác nhận đưa vào thùng rác?" message={`Bạn có chắc chắn muốn đưa nhân viên "${selectedItem?.fullName}" vào thùng rác không?`} />
        </>
    );
}
