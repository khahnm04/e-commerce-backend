"use client";
import React from "react";
import { ActionGroup } from "@/components/common/ActionGroup";
import { TableCell } from "@/components/common/TableCell";
import { TableCheckbox } from "@/components/common/TableCheckbox";
import { TableImage } from "@/components/common/TableImage";
import { UserTimestamp } from "@/components/common/UserTimestamp";
import { CustomSelect } from "@/components/common/CustomSelect";
import { EMPLOYEE_STATUS_OPTIONS } from "@/constants/user/employee.constants";
import { EmployeeResponse } from "@/types/user/employee.types";
import { getStatusColorClass } from "@/lib/format-utils";

interface EmployeeTableRowProps {
  item: EmployeeResponse;
  isTrash?: boolean;
  onDeleteClick?: () => void;
  onRestoreClick?: () => void;
  onStatusChange?: (id: string | number, newStatus: string) => void;
  isSelected?: boolean;
  onSelect?: () => void;
}

const EmployeeTableRowInner = ({
  item,
  isTrash = false,
  onDeleteClick,
  onRestoreClick,
  onStatusChange,
  isSelected,
  onSelect,
}: EmployeeTableRowProps) => {
  const updateStatusOptions = EMPLOYEE_STATUS_OPTIONS.filter((opt) => opt.id !== "");

  return (
    <tr>
      <TableCell>
        <TableCheckbox checked={isSelected} onChange={onSelect} />
      </TableCell>

      <TableCell>{item.code}</TableCell>

      <TableCell>{item.fullName}</TableCell>

      <TableCell>
        <TableImage src={item.avatarUrl} alt={item.fullName} />
      </TableCell>

      <TableCell>{item.phoneNumber}</TableCell>

      <TableCell>{item.email}</TableCell>

      <TableCell>{item.jobTitle}</TableCell>

      <TableCell>{item.branch}</TableCell>

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
        <UserTimestamp user={String(item.createdBy)} date={item.createdAt} />
      </TableCell>

      <TableCell>
        <UserTimestamp user={String(item.updatedBy)} date={item.updatedAt} />
      </TableCell>

      <TableCell>
        <ActionGroup
          isTrash={isTrash}
          editLink={isTrash ? undefined : `/employees/${item.id}`}
          viewLink={isTrash ? `/employees/trash/${item.id}` : undefined}
          onDelete={onDeleteClick || (() => {})}
          onRestore={onRestoreClick || (() => {})}
          hideDelete={isTrash}
        />
      </TableCell>
    </tr>
  );
};

export const EmployeeTableRow = React.memo(EmployeeTableRowInner);
EmployeeTableRowInner.displayName = "EmployeeTableRow";
