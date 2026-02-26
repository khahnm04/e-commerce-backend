/* eslint-disable @typescript-eslint/no-explicit-any */
"use client";
import React from "react";
import { Controller, Control, FieldErrors } from "react-hook-form";
import { RoleResponse } from "@/types/auth/role.types";

interface RoleSelectionFieldProps {
  control: Control<any>;
  name: string;
  allRoles: RoleResponse[];
  isReadOnly?: boolean;
  /** Role names to display in read-only mode */
  initialRoleNames?: string[];
  errors?: FieldErrors;
}

/**
 * Reusable role selection field with checkbox-style role badges.
 * Displays role names as read-only badges or editable checkboxes.
 * Used in EmployeeForm and potentially other user forms.
 */
export const RoleSelectionField = ({
  control,
  name,
  allRoles,
  isReadOnly = false,
  initialRoleNames,
  errors,
}: RoleSelectionFieldProps) => {
  const fieldError = errors?.[name];

  return (
    <Controller
      control={control}
      name={name}
      render={({ field }) => (
        <div className="md:col-span-2">
          <label className="block font-[600] text-[14px] text-[#606060] mb-[11px]">
            Nhóm quyền
          </label>
          {isReadOnly ? (
            <div className="flex flex-wrap gap-[8px]">
              {initialRoleNames && initialRoleNames.length > 0 ? (
                initialRoleNames.map((roleName) => (
                  <span
                    key={roleName}
                    className="bg-blue-50 text-blue-700 border border-blue-200 rounded-[6px] px-[12px] py-[6px] text-[13px] font-[600]"
                  >
                    {roleName}
                  </span>
                ))
              ) : (
                <p className="text-gray-400 text-sm italic">Chưa gán quyền</p>
              )}
            </div>
          ) : (
            <div className="bg-[#F5F6FA] border border-[#D5D5D5] rounded-[4px] p-[16px] flex flex-wrap gap-[12px]">
              {allRoles.length === 0 ? (
                <p className="text-gray-400 text-sm italic">Đang tải danh sách quyền...</p>
              ) : (
                allRoles.map((role) => {
                  const isChecked = (field.value || []).includes(role.id);
                  return (
                    <label
                      key={role.id}
                      className={`
                        inline-flex items-center gap-[8px] cursor-pointer
                        border rounded-[6px] px-[14px] py-[8px]
                        text-[13px] font-[600] transition-all
                        ${isChecked
                          ? "bg-blue-50 border-blue-400 text-blue-700"
                          : "bg-white border-[#D5D5D5] text-[#606060] hover:border-blue-300 hover:bg-blue-50/50"
                        }
                      `}
                    >
                      <input
                        type="checkbox"
                        checked={isChecked}
                        className="w-[16px] h-[16px] accent-blue-600 cursor-pointer"
                        onChange={(e) => {
                          const current = field.value || [];
                          if (e.target.checked) {
                            field.onChange([...current, role.id]);
                          } else {
                            field.onChange(current.filter((id: number) => id !== role.id));
                          }
                        }}
                      />
                      {role.name}
                    </label>
                  );
                })
              )}
            </div>
          )}
          {fieldError?.message && (
            <p className="text-red-500 text-[13px] mt-[6px]">{fieldError.message as string}</p>
          )}
        </div>
      )}
    />
  );
};
