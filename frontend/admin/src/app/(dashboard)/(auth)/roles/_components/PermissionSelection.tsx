import React, { useState, useMemo } from "react";
import { FaChevronDown } from "react-icons/fa6";
import { PermissionResponse } from "@/types/auth/role.types";

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

  // Group permissions by module, then by sub-prefix
  const groupedPermissions = useMemo(() => {
    const groups: Record<string, Record<string, PermissionResponse[]>> = {};
    permissions.forEach(p => {
      const moduleName = p.module || "Khác";
      if (!groups[moduleName]) groups[moduleName] = {};

      const parts = p.code.split('_');
      const subName = parts.length > 1 ? parts.slice(0, parts.length - 1).join('_') : "Chung";

      if (!groups[moduleName][subName]) groups[moduleName][subName] = [];
      groups[moduleName][subName].push(p);
    });
    return groups;
  }, [permissions]);

  const handleCheckboxChange = (id: number, checked: boolean) => {
    if (checked) {
      onChange([...selectedPermissions, id]);
    } else {
      onChange(selectedPermissions.filter((val: number) => val !== id));
    }
  };

  const handleSelectAll = (checked: boolean) => {
    if (checked) {
      onChange(permissions.map(p => p.id));
    } else {
      onChange([]);
    }
  };

  const toggleModule = (module: string) => {
    setExpandedModules(prev => {
      const next = new Set(prev);
      if (next.has(module)) {
        next.delete(module);
      } else {
        next.add(module);
      }
      return next;
    });
  };

  const toggleSubGroup = (subGroupKey: string) => {
    setExpandedSubGroups(prev => {
      const next = new Set(prev);
      if (next.has(subGroupKey)) {
        next.delete(subGroupKey);
      } else {
        next.add(subGroupKey);
      }
      return next;
    });
  };

  const isModuleAllSelected = (modulePerms: PermissionResponse[]) => {
    return modulePerms.every(p => selectedPermissions.includes(p.id));
  };

  const isModulePartialSelected = (modulePerms: PermissionResponse[]) => {
    const selected = modulePerms.filter(p => selectedPermissions.includes(p.id));
    return selected.length > 0 && selected.length < modulePerms.length;
  };

  const handleModuleSelectAll = (modulePerms: PermissionResponse[], checked: boolean) => {
    const moduleIds = modulePerms.map(p => p.id);
    if (checked) {
      const newIds = [...new Set([...selectedPermissions, ...moduleIds])];
      onChange(newIds);
    } else {
      onChange(selectedPermissions.filter(id => !moduleIds.includes(id)));
    }
  };

  return (
    <div className="border border-[#D5D5D5] rounded-[10px] bg-[#F5F6FA] p-4 max-h-[500px] overflow-y-auto w-full">
      {/* Select All */}
      <div className="mb-3 pb-3 border-b border-[#D5D5D5] flex items-center justify-between">
        <span className="font-semibold text-[14px]">Chọn tất cả</span>
        <input
          type="checkbox"
          className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
          checked={selectedPermissions.length === permissions.length && permissions.length > 0}
          onChange={(e) => handleSelectAll(e.target.checked)}
          disabled={isReadOnly}
        />
      </div>

      {/* Grouped by module */}
      <div className="space-y-4">
        {Object.entries(groupedPermissions).map(([module, subGroups]) => {
          const isExpanded = expandedModules.has(module);
          const modulePerms = Object.values(subGroups).flat();
          const allSelected = isModuleAllSelected(modulePerms);
          const partialSelected = isModulePartialSelected(modulePerms);

          return (
            <div key={module} className="border border-[#E0E0E0] rounded-[8px] bg-white overflow-hidden">
              {/* Module header */}
              <div
                className="flex items-center justify-between px-4 py-3 cursor-pointer hover:bg-gray-50 transition-colors"
                onClick={() => toggleModule(module)}
              >
                <div className="flex items-center gap-3">
                  <FaChevronDown
                    className={`text-[12px] text-gray-500 transition-transform duration-200 ${isExpanded ? "" : "-rotate-90"}`}
                  />
                  <span className="font-[600] text-[14px] text-[var(--color-text)]">
                    {module}
                  </span>
                  <span className="text-[12px] text-gray-400">
                    ({modulePerms.filter(p => selectedPermissions.includes(p.id)).length}/{modulePerms.length})
                  </span>
                </div>
                <input
                  type="checkbox"
                  className="w-[18px] h-[18px] cursor-pointer accent-blue-600"
                  checked={allSelected}
                  disabled={isReadOnly}
                  ref={el => {
                    if (el) el.indeterminate = partialSelected;
                  }}
                  onChange={(e) => {
                    e.stopPropagation();
                    handleModuleSelectAll(modulePerms, e.target.checked);
                  }}
                  onClick={(e) => e.stopPropagation()}
                />
              </div>

              {/* Permissions list */}
              {isExpanded && (
                <div className="px-4 pb-4 pt-3 border-t border-[#F0F0F0] space-y-3 bg-[#F9FAFB]">
                  {Object.entries(subGroups).map(([subName, perms]) => {
                    const subGroupKey = `${module}-${subName}`;
                    const isSubExpanded = expandedSubGroups.has(subGroupKey);
                    const allSubSelected = isModuleAllSelected(perms);
                    const partialSubSelected = isModulePartialSelected(perms);

                    return (
                      <div key={subName} className="border border-[#E5E7EB] rounded-[6px] bg-white overflow-hidden shadow-sm">
                        <div
                          className="flex items-center justify-between px-3 py-2 border-b border-[#E5E7EB] bg-[#F3F4F6] cursor-pointer hover:bg-[#EAEBEE] transition-colors"
                          onClick={() => toggleSubGroup(subGroupKey)}
                        >
                          <div className="flex items-center gap-2">
                            <FaChevronDown
                              className={`text-[10px] text-gray-500 transition-transform duration-200 ${isSubExpanded ? "" : "-rotate-90"}`}
                            />
                            <span className="font-semibold text-[13px] text-gray-700">{subName}</span>
                            <span className="text-[11px] text-gray-400 font-normal">
                              ({perms.filter(p => selectedPermissions.includes(p.id)).length}/{perms.length})
                            </span>
                          </div>
                          <input
                            type="checkbox"
                            className="w-[16px] h-[16px] cursor-pointer accent-blue-600"
                            checked={allSubSelected}
                            disabled={isReadOnly}
                            ref={el => {
                              if (el) el.indeterminate = partialSubSelected;
                            }}
                            onChange={(e) => {
                              e.stopPropagation();
                              handleModuleSelectAll(perms, e.target.checked);
                            }}
                            onClick={(e) => e.stopPropagation()}
                          />
                        </div>

                        {isSubExpanded && (
                          <div className="p-3 grid grid-cols-1 md:grid-cols-2 gap-2">
                            {perms.map((p) => (
                              <div key={p.id} className="flex items-center space-x-2 py-1">
                                <input
                                  type="checkbox"
                                  id={`perm-${p.id}`}
                                  value={p.id}
                                  checked={selectedPermissions.includes(p.id)}
                                  onChange={(e) => handleCheckboxChange(p.id, e.target.checked)}
                                  className="w-4 h-4 cursor-pointer accent-blue-600"
                                  disabled={isReadOnly}
                                />
                                <label htmlFor={`perm-${p.id}`} className="text-sm font-medium text-gray-700 cursor-pointer">
                                  {p.name}
                                </label>
                              </div>
                            ))}
                          </div>
                        )}
                      </div>
                    );
                  })}
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};
