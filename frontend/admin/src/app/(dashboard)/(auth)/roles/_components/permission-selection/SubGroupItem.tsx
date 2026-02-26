import React from "react";
import { FaChevronDown } from "react-icons/fa6";
import { PermissionResponse } from "@/types/auth/role.types";
import { PermissionItem } from "./PermissionItem";
import { countSelected, isGroupAllSelected, isGroupPartialSelected } from "./permission-selection.utils";

interface SubGroupItemProps {
  subName: string;
  perms: PermissionResponse[];
  selectedSet: Set<number>;
  isExpanded: boolean;
  isReadOnly: boolean;
  onToggleExpand: (key: string) => void;
  onGroupSelectAll: (perms: PermissionResponse[], checked: boolean) => void;
  onTogglePermission: (id: number, checked: boolean) => void;
  groupKey: string;
}

export const SubGroupItem = React.memo<SubGroupItemProps>(
  ({
    subName,
    perms,
    selectedSet,
    isExpanded,
    isReadOnly,
    onToggleExpand,
    onGroupSelectAll,
    onTogglePermission,
    groupKey,
  }) => {
    const selectedCount = countSelected(perms, selectedSet);
    const allSelected = isGroupAllSelected(perms, selectedSet);
    const partialSelected = isGroupPartialSelected(perms, selectedSet);

    return (
      <div className="border border-[#E5E7EB] rounded-[6px] bg-white overflow-hidden shadow-sm">
        <div
          className="flex items-center justify-between px-3 py-2 border-b border-[#E5E7EB] bg-[#F3F4F6] cursor-pointer hover:bg-[#EAEBEE] transition-colors"
          onClick={() => onToggleExpand(groupKey)}
        >
          <div className="flex items-center gap-2">
            <FaChevronDown
              className={`text-[10px] text-gray-500 transition-transform duration-200 ${isExpanded ? "" : "-rotate-90"}`}
            />
            <span className="font-semibold text-[13px] text-gray-700">
              {subName}
            </span>
            <span className="text-[11px] text-gray-400 font-normal">
              ({selectedCount}/{perms.length})
            </span>
          </div>
          <input
            type="checkbox"
            className="w-[16px] h-[16px] cursor-pointer accent-blue-600"
            checked={allSelected}
            disabled={isReadOnly}
            ref={(el) => {
              if (el) el.indeterminate = partialSelected;
            }}
            onChange={(e) => {
              e.stopPropagation();
              onGroupSelectAll(perms, e.target.checked);
            }}
            onClick={(e) => e.stopPropagation()}
          />
        </div>

        {isExpanded && (
          <div className="p-3 grid grid-cols-1 md:grid-cols-2 gap-2">
            {perms.map((p) => (
              <PermissionItem
                key={p.id}
                permission={p}
                isSelected={selectedSet.has(p.id)}
                isReadOnly={isReadOnly}
                onToggle={onTogglePermission}
              />
            ))}
          </div>
        )}
      </div>
    );
  },
);
SubGroupItem.displayName = "SubGroupItem";
