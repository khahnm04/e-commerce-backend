"use client";
import { useState, useEffect, useCallback } from "react";
import { ApiResponse, PageMetadata } from "@/types/common/api.types";

interface UseApiDataOptions<T> {
  fetchFn: () => Promise<ApiResponse<T[]>>;
  deps?: unknown[];
}

export function useApiData<T>({ fetchFn, deps = [] }: UseApiDataOptions<T>) {
  const [data, setData] = useState<T[]>([]);
  const [meta, setMeta] = useState<PageMetadata | undefined>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const reload = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetchFn();
      setData(res.data ?? []);
      setMeta(res.meta);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Có lỗi xảy ra");
      setData([]);
    } finally {
      setLoading(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [fetchFn, ...deps]);

  useEffect(() => { reload(); }, [reload]);

  return { data, meta, loading, error, reload };
}
