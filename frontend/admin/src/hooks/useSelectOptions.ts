"use client";
import { useState, useEffect, useRef, useCallback } from "react";
import { ApiResponse } from "@/types/common/api.types";

/**
 * A reusable hook for fetching select options from the API.
 * Caches the result so it doesn't re-fetch unnecessarily.
 * Uses refs for fetchFn/mapFn to avoid dependency instability.
 * Handles React Strict Mode double-mount and token refresh race conditions.
 */
interface SelectOption {
  id: string | number;
  name: string;
}

const ALL_OPTION: SelectOption = { id: "", name: "Tất cả" };
const MAX_OPTIONS_SIZE = 99;
const RETRY_DELAY_MS = 1500;

interface UseSelectOptionsConfig<T> {
  fetchFn: (params: Record<string, unknown>) => Promise<ApiResponse<T[]>>;
  mapFn: (item: T) => SelectOption;
  includeAllOption?: boolean;
}

export function useSelectOptions<T>({
  fetchFn,
  mapFn,
  includeAllOption = true,
}: UseSelectOptionsConfig<T>) {
  const [options, setOptions] = useState<SelectOption[]>(includeAllOption ? [ALL_OPTION] : []);

  // Use refs so that callback identity changes don't trigger re-fetches
  const fetchFnRef = useRef(fetchFn);
  const mapFnRef = useRef(mapFn);
  useEffect(() => {
    fetchFnRef.current = fetchFn;
    mapFnRef.current = mapFn;
  });

  const loadOptions = useCallback(
    async (signal: AbortSignal) => {
      try {
        const res = await fetchFnRef.current({ size: MAX_OPTIONS_SIZE });
        if (signal.aborted) return;
        const mapped = (res.data ?? []).map(mapFnRef.current);
        setOptions(includeAllOption ? [ALL_OPTION, ...mapped] : mapped);
      } catch {
        if (signal.aborted) return;
        // Retry once after delay (handles token refresh race condition)
        setTimeout(async () => {
          if (signal.aborted) return;
          try {
            const res = await fetchFnRef.current({ size: MAX_OPTIONS_SIZE });
            if (signal.aborted) return;
            const mapped = (res.data ?? []).map(mapFnRef.current);
            setOptions(includeAllOption ? [ALL_OPTION, ...mapped] : mapped);
          } catch {
            // Give up silently after retry
          }
        }, RETRY_DELAY_MS);
      }
    },
    [includeAllOption],
  );

  useEffect(() => {
    const abortController = new AbortController();
    loadOptions(abortController.signal);
    return () => {
      abortController.abort();
    };
  }, [loadOptions]);

  return options;
}
