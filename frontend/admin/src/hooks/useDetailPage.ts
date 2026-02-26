"use client";
import { useState, useEffect, useRef } from "react";
import { ApiResponse } from "@/types/common/api.types";

interface UseDetailPageConfig<T> {
  fetchFn: (id: string | number) => Promise<ApiResponse<T>>;
  id: string | number;
}

export function useDetailPage<T>({ fetchFn, id }: UseDetailPageConfig<T>) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchFnRef = useRef(fetchFn);
  fetchFnRef.current = fetchFn;

  useEffect(() => {
    setLoading(true);
    const fetchData = async () => {
      try {
        const res = await fetchFnRef.current(id);
        setData(res.data);
      } catch {
        setData(null);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  return { data, loading };
}
