/**
 * Shared formatting utilities for the admin frontend.
 * Centralizes reusable formatting logic that was previously duplicated.
 */

/**
 * Status → CSS color class mapping.
 * Used across all TableRow components to style status indicators.
 */
const STATUS_COLOR_MAP: Record<string, string> = {
    ACTIVE: "text-[#10B981]",
    INACTIVE: "text-[#F59E0B]",
    OUT_OF_STOCK: "text-[#EF4444]",
    PENDING: "text-[#3B82F6]",
    DISCONTINUED: "text-[#6B7280]",
    BANNED: "text-[#EF4444]",
    LOCKED: "text-[#6B7280]",
};

export function getStatusColorClass(status: string): string {
    return STATUS_COLOR_MAP[status] ?? "";
}

/**
 * Format a number as Vietnamese currency (VND).
 * Used in ProductTableRow and ProductVariantTableRow.
 */
const VND_FORMATTER = new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
});

export function formatCurrency(value: number): string {
    return VND_FORMATTER.format(value);
}

/**
 * Gender label mapping.
 * Used in CustomerTableRow.
 */
const GENDER_LABEL_MAP: Record<string, string> = {
    MALE: "Nam",
    FEMALE: "Nữ",
    OTHER: "Khác",
};

export function getGenderLabel(gender: string): string {
    return GENDER_LABEL_MAP[gender] ?? gender;
}

/**
 * Generate a URL-friendly slug from a string (including Vietnamese).
 */
export function generateSlug(text: string): string {
    if (!text) return "";
    return text
        .toString()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "") // remove diacritics
        .toLowerCase()
        .replace(/đ/g, "d")
        .trim()
        .replace(/\s+/g, "-") // replace spaces with -
        .replace(/[^\w-]+/g, "") // remove all non-word chars
        .replace(/--+/g, "-") // replace multiple - with single -
        .replace(/^-+/, "") // trim - from start of text
        .replace(/-+$/, ""); // trim - from end of text
}
