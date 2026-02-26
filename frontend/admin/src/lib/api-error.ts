import { toast } from "react-hot-toast";

/**
 * Standardized API error handler.
 * Extracts the error message and shows a toast notification.
 * Solves issue #10: Non-standardized error handling.
 */
export function handleApiError(error: unknown, fallbackMessage: string = "Có lỗi xảy ra!"): void {
  const message = error instanceof Error ? error.message : fallbackMessage;
  toast.error(message);
}

/**
 * Wraps an async API call with standardized error handling and optional success toast.
 * Returns true if the operation succeeded, false otherwise.
 */
export async function withApiHandler(
  fn: () => Promise<unknown>,
  options?: {
    successMessage?: string;
    errorMessage?: string;
  },
): Promise<boolean> {
  try {
    await fn();
    if (options?.successMessage) {
      toast.success(options.successMessage);
    }
    return true;
  } catch (error: unknown) {
    handleApiError(error, options?.errorMessage);
    return false;
  }
}
