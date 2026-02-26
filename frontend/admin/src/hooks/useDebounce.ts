"use client";
import { useState, useEffect } from "react";

/**
 * A reusable debounce hook.
 * Returns the debounced value after the specified delay.
 * Cancels the previous timer on each new value change.
 */
export function useDebounce<T>(value: T, delay: number = 500): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => clearTimeout(timer);
  }, [value, delay]);

  return debouncedValue;
}
