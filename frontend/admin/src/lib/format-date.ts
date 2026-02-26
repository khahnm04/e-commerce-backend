/**
 * Parse an ISO date string consistently across browsers.
 * Handles strings with or without timezone suffix.
 * Strings without 'Z' or '+/-' offset are treated as local time.
 */
function parseDate(dateStr: string): Date | null {
  if (!dateStr) return null;
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return null;
  return date;
}

/**
 * Format an ISO date string to a human-readable Vietnamese format
 * e.g. "2026-02-24T15:26:57" → "24/02/2026 15:26"
 */
export function formatDateTime(dateStr?: string | null): string {
  if (!dateStr) return "";

  const date = parseDate(dateStr);
  if (!date) return dateStr;

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${day}/${month}/${year} ${hours}:${minutes}`;
}

/**
 * Format an ISO date string to date-only
 * e.g. "2026-02-24T15:26:57" → "24/02/2026"
 */
export function formatDate(dateStr?: string | null): string {
  if (!dateStr) return "";

  const date = parseDate(dateStr);
  if (!date) return dateStr;

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();

  return `${day}/${month}/${year}`;
}
