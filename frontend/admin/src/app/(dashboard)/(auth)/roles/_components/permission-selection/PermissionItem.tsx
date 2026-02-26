import React from "react";

interface PermissionItemProps {
  permission: { id: number; name: string };
  isSelected: boolean;
  isReadOnly: boolean;
  onToggle: (id: number, checked: boolean) => void;
}

export const PermissionItem = React.memo<PermissionItemProps>(
  ({ permission, isSelected, isReadOnly, onToggle }) => (
    <div className="flex items-center space-x-2 py-1">
      <input
        type="checkbox"
        id={`perm-${permission.id}`}
        value={permission.id}
        checked={isSelected}
        onChange={(e) => onToggle(permission.id, e.target.checked)}
        className="w-4 h-4 cursor-pointer accent-blue-600"
        disabled={isReadOnly}
      />
      <label
        htmlFor={`perm-${permission.id}`}
        className="text-sm font-medium text-gray-700 cursor-pointer"
      >
        {permission.name}
      </label>
    </div>
  ),
);
PermissionItem.displayName = "PermissionItem";
