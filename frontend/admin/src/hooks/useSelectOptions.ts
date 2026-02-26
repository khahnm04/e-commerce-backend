"use client";
import { useState, useEffect, useRef } from "react";
import { ApiResponse } from "@/types/common/api.types";

/**
 * A reusable hook for fetching select options from the API.
 * Caches the result so it doesn't re-fetch unnecessarily.
 * Uses refs for fetchFn/mapFn to avoid dependency instability.
 */
interface SelectOption {
  id: string | number;
  name: string;
}

const ALL_OPTION: SelectOption = { id: "", name: "Tất cả" };
const MAX_OPTIONS_SIZE = 999;

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
  const hasFetched = useRef(false);

  // Use refs so that callback identity changes don't trigger re-fetches
  const fetchFnRef = useRef(fetchFn);
  const mapFnRef = useRef(mapFn);
  useEffect(() => {
    fetchFnRef.current = fetchFn;
    mapFnRef.current = mapFn;
  });

  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;

    const load = async () => {
      try {
        const res = await fetchFnRef.current({ size: MAX_OPTIONS_SIZE });
        const mapped = (res.data ?? []).map(mapFnRef.current);
        setOptions(includeAllOption ? [ALL_OPTION, ...mapped] : mapped);
      } catch {
        // Silently ignore fetch errors for options
      }
    };
    load();
  }, [includeAllOption]);

  return options;
}
