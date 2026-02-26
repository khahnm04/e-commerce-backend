"use client";
import { useState, useCallback } from "react";
import { useRouter } from "next/navigation";
import { handleApiError } from "@/lib/api-error";
import { toast } from "react-hot-toast";

interface UseFormSubmitOptions {
  /** URL to redirect to after successful submission */
  redirectTo: string;
}

/**
 * Standardized form submission hook.
 * Handles loading state, error handling, success toast, and redirect.
 * Replaces the duplicated try/catch/finally pattern across all form components.
 */
export function useFormSubmit({ redirectTo }: UseFormSubmitOptions) {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);

  const submitHandler = useCallback(
    async (fn: () => Promise<void>, successMessage: string) => {
      try {
        setIsLoading(true);
        await fn();
        toast.success(successMessage);
        if (redirectTo) {
          router.push(redirectTo);
          router.refresh();
        }
      } catch (error: unknown) {
        handleApiError(error);
      } finally {
        setIsLoading(false);
      }
    },
    [router, redirectTo],
  );

  return { isLoading, submitHandler };
}
