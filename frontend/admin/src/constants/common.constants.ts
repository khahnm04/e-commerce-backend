/**
 * Common constants shared across all modules.
 * Eliminates duplication of identical values in per-module constant files.
 */

/** Common bulk trash action: restore */
export const TRASH_ACTION_OPTIONS = [{ id: "restore", name: "Khôi phục" }];

interface Option {
  id: string | number;
  name: string;
}

/**
 * Filter out the "Tất cả" option (id === "") from filter options,
 * so they can be reused as form options (no "all" choice).
 */
export function getFormOptions(options: Option[]): Option[] {
  return options.filter((opt) => opt.id !== "");
}

/** Gender options for forms (without "Tất cả") */
export const GENDER_FORM_OPTIONS = [
  { id: "MALE", name: "Nam" },
  { id: "FEMALE", name: "Nữ" },
  { id: "OTHER", name: "Khác" },
];
