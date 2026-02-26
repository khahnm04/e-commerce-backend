import { PermissionResponse } from "@/types/auth/role.types";

// --- Util: group permissions by module → subgroup ---
export type GroupedPermissions = Record<string, Record<string, PermissionResponse[]>>;

export const groupPermissionsByModule = (permissions: PermissionResponse[]): GroupedPermissions => {
  const groups: GroupedPermissions = {};
  permissions.forEach((p) => {
    const moduleName = p.module || "Khác";
    if (!groups[moduleName]) groups[moduleName] = {};

    const parts = p.code.split("_");
    const subName = parts.length > 1 ? parts.slice(0, parts.length - 1).join("_") : "Chung";

    if (!groups[moduleName][subName]) groups[moduleName][subName] = [];
    groups[moduleName][subName].push(p);
  });
  return groups;
};

// --- Shared helpers (pure functions) ---
export const countSelected = (perms: PermissionResponse[], selectedSet: Set<number>): number =>
  perms.reduce((count, p) => (selectedSet.has(p.id) ? count + 1 : count), 0);

export const isGroupAllSelected = (perms: PermissionResponse[], selectedSet: Set<number>): boolean =>
  perms.length > 0 && perms.every((p) => selectedSet.has(p.id));

export const isGroupPartialSelected = (perms: PermissionResponse[], selectedSet: Set<number>): boolean => {
  const count = countSelected(perms, selectedSet);
  return count > 0 && count < perms.length;
};
