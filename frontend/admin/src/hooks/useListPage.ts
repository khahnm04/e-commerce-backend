"use client";
import { useState, useCallback, useRef, useMemo } from "react";
import { toast } from "react-hot-toast";
import { ApiResponse } from "@/types/common/api.types";
import { useDebounce } from "./useDebounce";
import { useApiData } from "./useApiData";
import { withApiHandler } from "@/lib/api-error";

// ==========================================
// Types
// ==========================================

type FilterValue = string | number;

export interface FilterState {
    [key: string]: FilterValue;
}

interface DateRange {
    startDate: string;
    endDate: string;
}

interface ListPageService<T, F> {
    getAll: (filter?: F) => Promise<ApiResponse<T[]>>;
    getAllDeleted: (filter?: F) => Promise<ApiResponse<T[]>>;
    softDelete?: (id: number | string) => Promise<ApiResponse<void>>;
    restore?: (id: number | string) => Promise<ApiResponse<void>>;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    updateStatus?: (id: number | string, payload: any) => Promise<ApiResponse<void>>;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    bulkAction?: (payload: any) => Promise<ApiResponse<void>>;
    /** Alias for hard delete (e.g., roleService.delete) */
    delete?: (id: number | string) => Promise<ApiResponse<void>>;
}

interface UseListPageConfig<T, F> {
    service: ListPageService<T, F>;
    isTrash?: boolean;
    /** Name of the entity for toast messages (e.g., "sản phẩm", "nhãn hàng") */
    entityName: string;
    /** Key name for bulk action payload - the field that lists item IDs (e.g., "products", "brands") */
    bulkActionKey: string;
    /** Custom function to build filter params from current state */
    buildFilterParams?: (params: {
        keyword?: string;
        page: number;
        size: number;
        filters: FilterState;
        dateRange: DateRange;
    }) => Record<string, unknown>;
    /** Initial page size, defaults to 10 */
    initialPageSize?: number;
}

// ==========================================
// Hook
// ==========================================

export function useListPage<T extends { id: number | string }, F>({
    service,
    isTrash = false,
    entityName,
    bulkActionKey,
    buildFilterParams: customBuildParams,
    initialPageSize = 10,
}: UseListPageConfig<T, F>) {
    // --- Search ---
    const [searchTerm, setSearchTerm] = useState("");
    const debouncedSearch = useDebounce(searchTerm);

    // --- Stable Refs for methods ---
    const serviceRef = useRef(service);
    const customBuildParamsRef = useRef(customBuildParams);

    // Always keep refs up to date
    serviceRef.current = service;
    customBuildParamsRef.current = customBuildParams;

    // --- Pagination ---
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(initialPageSize);

    // --- Filters (consolidated into one object) ---
    const [filters, setFilters] = useState<FilterState>({});
    const [dateRange, setDateRange] = useState<DateRange>({
        startDate: "",
        endDate: "",
    });

    // --- Bulk action ---
    const [actionFilter, setActionFilter] = useState<FilterValue>("");

    // --- Selection (using Set for O(1) lookup) ---
    const [selectedSet, setSelectedSet] = useState<Set<string | number>>(new Set());

    // --- Modal ---
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedItem, setSelectedItem] = useState<T | null>(null);

    // --- Loading/first load ---
    const isFirstLoad = useRef(true);

    // ==========================================
    // Handlers that reset page
    // ==========================================

    /** Update a single filter value and reset page to 1 */
    const setFilter = useCallback((key: string, value: FilterValue) => {
        setFilters(prev => ({ ...prev, [key]: value }));
        setPage(1);
    }, []);

    const setStartDate = useCallback((val: string) => {
        setDateRange(prev => ({ ...prev, startDate: val }));
        setPage(1);
    }, []);

    const setEndDate = useCallback((val: string) => {
        setDateRange(prev => ({ ...prev, endDate: val }));
        setPage(1);
    }, []);

    const handleSearchChange = useCallback((val: string) => {
        setSearchTerm(val);
        setPage(1);
    }, []);

    const handlePageSizeChange = useCallback((size: number) => {
        setPageSize(size);
        setPage(1);
    }, []);

    const handleClearFilter = useCallback(() => {
        setFilters({});
        setDateRange({ startDate: "", endDate: "" });
        setPage(1);
    }, []);

    // ==========================================
    // Data fetching
    // ==========================================

    const fetchFn = useCallback(() => {
        const baseParams = {
            keyword: debouncedSearch || undefined,
            page,
            size: pageSize,
            startDate: dateRange.startDate || undefined,
            endDate: dateRange.endDate || undefined,
        };

        if (customBuildParamsRef.current) {
            const filterParams = customBuildParamsRef.current({
                keyword: debouncedSearch || undefined,
                page,
                size: pageSize,
                filters,
                dateRange,
            }) as F;
            return isTrash
                ? serviceRef.current.getAllDeleted(filterParams)
                : serviceRef.current.getAll(filterParams);
        }

        // Default: merge base params with filter values
        const filterEntries: Record<string, unknown> = { ...baseParams };
        Object.entries(filters).forEach(([key, value]) => {
            if (value !== "" && value !== undefined) {
                filterEntries[key] = value;
            }
        });

        return isTrash
            ? serviceRef.current.getAllDeleted(filterEntries as F)
            : serviceRef.current.getAll(filterEntries as F);
    }, [
        isTrash,
        debouncedSearch,
        filters,
        dateRange,
        page,
        pageSize,
    ]);

    const { data, meta, loading, reload } = useApiData<T>({ fetchFn });

    // Track first load
    if (!loading && isFirstLoad.current) {
        isFirstLoad.current = false;
    }

    // ==========================================
    // Selection logic (Set-based for O(1))
    // ==========================================

    const selectedIds = useMemo(
        () => Array.from(selectedSet),
        [selectedSet]
    );

    const isSelected = useCallback(
        (id: string | number) => selectedSet.has(id),
        [selectedSet]
    );

    const handleSelectAll = useCallback(() => {
        if (selectedSet.size === data.length && data.length > 0) {
            setSelectedSet(new Set());
        } else {
            setSelectedSet(new Set(data.map(item => item.id)));
        }
    }, [selectedSet.size, data]);

    const handleSelectItem = useCallback((id: string | number) => {
        setSelectedSet(prev => {
            const next = new Set(prev);
            if (next.has(id)) {
                next.delete(id);
            } else {
                next.add(id);
            }
            return next;
        });
    }, []);

    const isSelectAll = data.length > 0 && selectedSet.size === data.length;

    // ==========================================
    // Modal
    // ==========================================

    const handleOpenDeleteModal = useCallback((item: T) => {
        setSelectedItem(item);
        setIsModalOpen(true);
    }, []);

    const handleCloseModal = useCallback(() => {
        setIsModalOpen(false);
        setSelectedItem(null);
    }, []);

    // ==========================================
    // CRUD operations with standardized error handling
    // ==========================================

    const handleConfirmDelete = useCallback(async () => {
        if (!selectedItem) {
            handleCloseModal();
            return;
        }

        const deleteFn = serviceRef.current.softDelete ?? serviceRef.current.delete;
        if (deleteFn) {
            await deleteFn(selectedItem.id);
            toast.success(`Xóa ${entityName} thành công!`);
            reload();
        }
        handleCloseModal();
    }, [selectedItem, entityName, reload, handleCloseModal]);

    const handleRestore = useCallback(async (id: number) => {
        if (!serviceRef.current.restore) return;
        const success = await withApiHandler(
            () => serviceRef.current.restore!(id),
            {
                successMessage: `Khôi phục ${entityName} thành công!`,
                errorMessage: `Có lỗi xảy ra khi khôi phục!`,
            }
        );
        if (success) reload();
    }, [entityName, reload]);

    const handleStatusChange = useCallback(async (id: string | number, newStatus: string) => {
        if (!serviceRef.current.updateStatus) return;
        const success = await withApiHandler(
            () => serviceRef.current.updateStatus!(id, { status: newStatus }),
            {
                successMessage: "Cập nhật trạng thái thành công!",
                errorMessage: "Có lỗi xảy ra!",
            }
        );
        if (success) reload();
    }, [reload]);

    const handleApplyAction = useCallback(async () => {
        if (!actionFilter || selectedIds.length === 0) {
            toast.error(`Vui lòng chọn ít nhất 1 ${entityName} và hành động!`);
            return;
        }
        if (!serviceRef.current.bulkAction) return;

        const success = await withApiHandler(
            () => serviceRef.current.bulkAction!({
                [bulkActionKey]: selectedIds.map(id => Number(id)),
                action: String(actionFilter),
            }),
            {
                successMessage: "Thực hiện hành động thành công!",
                errorMessage: "Có lỗi xảy ra!",
            }
        );
        if (success) {
            setSelectedSet(new Set());
            setActionFilter("");
            reload();
        }
    }, [actionFilter, selectedIds, entityName, bulkActionKey, reload]);

    // ==========================================
    // Return
    // ==========================================

    return {
        // Search
        searchTerm, handleSearchChange,

        // Pagination
        page, setPage, pageSize, handlePageSizeChange,

        // Filters
        filters, setFilter, dateRange,
        setStartDate, setEndDate,
        handleClearFilter,

        // Bulk action
        actionFilter, setActionFilter,

        // Data
        data, meta, loading, reload,
        isFirstLoad: isFirstLoad.current,

        // Selection
        selectedIds, isSelected, isSelectAll,
        handleSelectAll, handleSelectItem,

        // Modal
        isModalOpen, selectedItem,
        handleOpenDeleteModal, handleCloseModal,

        // CRUD
        handleConfirmDelete,
        handleRestore,
        handleStatusChange,
        handleApplyAction,
    };
}
