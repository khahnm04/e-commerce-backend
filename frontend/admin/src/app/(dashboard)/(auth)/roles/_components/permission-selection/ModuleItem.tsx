import React, { useMemo } from "react";
import { FaChevronDown } from "react-icons/fa6";
import { PermissionResponse } from "@/types/auth/role.types";
import { countSelected, isGroupAllSelected, isGroupPartialSelected } from "./permission-selection.utils";
import { SubGroupItem } from "./SubGroupItem";

// --- ModuleItem ---

interface ModuleItemProps {
  module: string;
  subGroups: Record<string, PermissionResponse[]>;
  selectedSet: Set<number>;
  isExpanded: boolean;
  expandedSubGroups: Set<string>;
  isReadOnly: boolean;
  onToggleModule: (module: string) => void;
  onToggleSubGroup: (key: string) => void;
  onGroupSelectAll: (perms: PermissionResponse[], checked: boolean) => void;
  onTogglePermission: (id: number, checked: boolean) => void;
}

export const ModuleItem = React.memo<ModuleItemProps>(
  ({
    module,
    subGroups,
    selectedSet,
    isExpanded,
    expandedSubGroups,
    isReadOnly,
    onToggleModule,
    onToggleSubGroup,
    onGroupSelectAll,
    onTogglePermission,
  }) => {
    const modulePerms = useMemo(() => Object.values(subGroups).flat(), [subGroups]);
    const selectedCount = countSelected(modulePerms, selectedSet);
    const allSelected = isGroupAllSelected(modulePerms, selectedSet);
    const partialSelected = isGroupPartialSelected(modulePerms, selectedSet);

    return (
      <div className="border border-[#E0E0E0] rounded-[8px] bg-white overflow-hidden">
        {/* Module header */}
        <div
          className="flex items-center justify-between px-4 py-3 cursor-pointer hover:bg-gray-50 transition-colors"
          onClick={() => onToggleModule(module)}
        >
          <div className="flex items-center gap-3">
            <FaChevronDown
              className={`text-[12px] text-gray-500 transition-transform duration-200 ${isExpanded ? "" : "-rotate-90"}`}
            />
            <span className="font-[600] text-[14px] text-[var(--color-text)]">
              {module}
            </span>
            <span className="text-[12px] text-gray-400">
              ({selectedCount}/{modulePerms.length})
            </span>
          </div>
          <input
            type="checkbox"
            className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
            checked={allSelected}
            disabled={isReadOnly}
            ref={(el) => {
              if (el) el.indeterminate = partialSelected;
            }}
            onChange={(e) => {
              e.stopPropagation();
              onGroupSelectAll(modulePerms, e.target.checked);
            }}
            onClick={(e) => e.stopPropagation()}
          />
        </div>

        {/* SubGroups list */}
        {isExpanded && (
          <div className="px-4 pb-4 pt-3 border-t border-[#F0F0F0] space-y-3 bg-[#F9FAFB]">
            {Object.entries(subGroups).map(([subName, perms]) => {
              const subGroupKey = `${module}-${subName}`;
              return (
                <SubGroupItem
                  key={subName}
                  subName={subName}
                  perms={perms}
                  selectedSet={selectedSet}
                  isExpanded={expandedSubGroups.has(subGroupKey)}
                  isReadOnly={isReadOnly}
                  onToggleExpand={onToggleSubGroup}
                  onGroupSelectAll={onGroupSelectAll}
                  onTogglePermission={onTogglePermission}
                  groupKey={subGroupKey}
                />
              );
            })}
          </div>
        )}
      </div>
    );
  },
);
ModuleItem.displayName = "ModuleItem";
