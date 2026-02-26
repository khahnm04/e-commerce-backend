import React, { useState, useMemo, useCallback } from "react";
import { PermissionResponse } from "@/types/auth/role.types";
import { ModuleItem } from "./ModuleItem";
import { groupPermissionsByModule } from "./permission-selection.utils";

interface PermissionSelectionProps {
  permissions: PermissionResponse[];
  selectedPermissions: number[];
  onChange: (ids: number[]) => void;
  isReadOnly?: boolean;
}

export const PermissionSelection: React.FC<PermissionSelectionProps> = ({
  permissions,
  selectedPermissions,
  onChange,
  isReadOnly = false,
}) => {
  const [expandedModules, setExpandedModules] = useState<Set<string>>(new Set());
  const [expandedSubGroups, setExpandedSubGroups] = useState<Set<string>>(new Set());

  // O(1) lookup instead of O(n) .includes()
  const selectedSet = useMemo(() => new Set(selectedPermissions), [selectedPermissions]);

  const groupedPermissions = useMemo(
    () => groupPermissionsByModule(permissions),
    [permissions],
  );

  const handleCheckboxChange = useCallback(
    (id: number, checked: boolean) => {
      if (checked) {
        const next = new Set(selectedPermissions);
        next.add(id);
        onChange([...next]);
      } else {
        onChange(selectedPermissions.filter((val) => val !== id));
      }
    },
    [selectedPermissions, onChange],
  );

  const handleSelectAll = useCallback(
    (checked: boolean) => {
      if (checked) {
        onChange(permissions.map((p) => p.id));
      } else {
        onChange([]);
      }
    },
    [permissions, onChange],
  );

  const toggleModule = useCallback((module: string) => {
    setExpandedModules((prev) => {
      const next = new Set(prev);
      if (next.has(module)) {
        next.delete(module);
      } else {
        next.add(module);
      }
      return next;
    });
  }, []);

  const toggleSubGroup = useCallback((subGroupKey: string) => {
    setExpandedSubGroups((prev) => {
      const next = new Set(prev);
      if (next.has(subGroupKey)) {
        next.delete(subGroupKey);
      } else {
        next.add(subGroupKey);
      }
      return next;
    });
  }, []);

  const handleGroupSelectAll = useCallback(
    (groupPerms: PermissionResponse[], checked: boolean) => {
      const groupIds = new Set(groupPerms.map((p) => p.id));
      if (checked) {
        const merged = new Set([...selectedPermissions, ...groupIds]);
        onChange([...merged]);
      } else {
        onChange(selectedPermissions.filter((id) => !groupIds.has(id)));
      }
    },
    [selectedPermissions, onChange],
  );

  return (
    <div className="border border-[#D5D5D5] rounded-[10px] bg-[#F5F6FA] p-4 max-h-[500px] overflow-y-auto w-full">
      {/* Select All */}
      <div className="mb-3 pb-3 border-b border-[#D5D5D5] flex items-center justify-between">
        <span className="font-semibold text-[14px]">Chọn tất cả</span>
        <input
          type="checkbox"
          className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
          checked={selectedSet.size === permissions.length && permissions.length > 0}
          onChange={(e) => handleSelectAll(e.target.checked)}
          disabled={isReadOnly}
        />
      </div>

      {/* Grouped by module */}
      <div className="space-y-4">
        {Object.entries(groupedPermissions).map(([module, subGroups]) => (
          <ModuleItem
            key={module}
            module={module}
            subGroups={subGroups}
            selectedSet={selectedSet}
            isExpanded={expandedModules.has(module)}
            expandedSubGroups={expandedSubGroups}
            isReadOnly={isReadOnly}
            onToggleModule={toggleModule}
            onToggleSubGroup={toggleSubGroup}
            onGroupSelectAll={handleGroupSelectAll}
            onTogglePermission={handleCheckboxChange}
          />
        ))}
      </div>
    </div>
  );
};
