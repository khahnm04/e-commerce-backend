
"use client";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { EMPLOYEE_STATUS_OPTIONS } from "@/constants/user/employee.constants";
import { EmployeeResponse } from "@/types/user/employee.types";

interface EmployeeTableRowProps {
    item: EmployeeResponse;
    isTrash?: boolean;
    onDeleteClick?: () => void;
    onRestoreClick?: () => void;
    onStatusChange?: (id: string | number, newStatus: string) => void;
    isSelected?: boolean;
    onSelect?: () => void;
}

export const EmployeeTableRow = ({
    item,
    isTrash = false,
    onDeleteClick,
    onRestoreClick,
    onStatusChange,
    isSelected,
    onSelect
}: EmployeeTableRowProps) => {

    const updateStatusOptions = EMPLOYEE_STATUS_OPTIONS.filter(opt => opt.id !== "");

    const getStatusColorClass = (status: string) => {
        switch (status) {
            case "ACTIVE":
                return "text-[#10B981]";
            case "INACTIVE":
                return "text-[#F59E0B]";
            case "BANNED":
                return "text-[#EF4444]";
            case "LOCKED":
                return "text-[#6B7280]";
            default:
                return "";
        }
    };

    return (
        <tr>
            <TableCell>
                <TableCheckbox
                    checked={isSelected}
                    onChange={onSelect}
                />
            </TableCell>

            <TableCell>
                {item.code}
            </TableCell>

            <TableCell>
                {item.fullName}
            </TableCell>

            <TableCell>
                <TableImage
                    src={item.avatarUrl}
                    alt={item.fullName}
                />
            </TableCell>

            <TableCell>
                {item.phoneNumber}
            </TableCell>

            <TableCell>
                {item.email}
            </TableCell>

            <TableCell>
                {item.jobTitle}
            </TableCell>

            <TableCell>
                {item.branch}
            </TableCell>

            <TableCell>
                <div className="flex justify-center items-center ml-[20px]">
                    <CustomSelect
                        classNameCustom={getStatusColorClass(item.status)}
                        value={item.status}
                        options={updateStatusOptions}
                        onChange={(newValue) => {
                            if (onStatusChange) {
                                onStatusChange(item.id, newValue as string);
                            }
                        }}
                        placeholder={item.status}
                    />
                </div>
            </TableCell>

            <TableCell>
                <UserTimestamp
                    user={String(item.createdBy)}
                    date={item.createdAt}
                />
            </TableCell>

            <TableCell>
                <UserTimestamp
                    user={String(item.updatedBy)}
                    date={item.updatedAt}
                />
            </TableCell>

            <td className="py-[8px] px-[15px] text-center border-b border-[rgba(151,151,151,0.6)] text-[14px] font-[600] text-[var(--color-text)]">
                <ActionGroup
                    isTrash={isTrash}
                    editLink={isTrash ? undefined : `/employees/${item.id}`}
                    viewLink={isTrash ? `/employees/trash/${item.id}` : undefined}
                    onDelete={onDeleteClick || (() => { })}
                    onRestore={onRestoreClick || (() => { })}
                    hideDelete={isTrash}
                />
            </td>
        </tr>
    );
};
