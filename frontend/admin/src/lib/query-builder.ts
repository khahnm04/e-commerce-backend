/**
 * Builds a URL query string from a filter object.
 * Filters out undefined, null, and empty string values.
 * Shared across all services instead of duplicated.
 * Solves issue #9 (magic values) and code duplication.
 */
export function buildQuery(filter?: Record<string, unknown>): string {
    if (!filter) return "";
    const params = new URLSearchParams();
    Object.entries(filter).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") {
            params.append(key, String(value));
        }
    });
    const q = params.toString();
    return q ? `?${q}` : "";
}

/**
 * Builds a filter params object from raw values, stripping out empty/undefined values.
 * This replaces the pattern of manually mapping filter values in components.
 */
export function buildFilterParams<T extends Record<string, unknown>>(
    params: T
): Partial<T> {
    const result: Partial<T> = {};
    for (const key in params) {
        const value = params[key];
        if (value !== undefined && value !== null && value !== "") {
            result[key] = value;
        }
    }
    return result;
}
