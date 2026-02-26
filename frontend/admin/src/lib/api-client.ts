let refreshPromise: Promise<boolean> | null = null;

const refreshToken = async (): Promise<boolean> => {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  try {
    const res = await fetch(`${baseUrl}/auth/refresh`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
    });

    if (!res.ok) {
      return false;
    }

    const data = await res.json();
    return data?.success === true;
  } catch {
    return false;
  }
};

export const sendRequest = async <T>(endpoint: string, options?: RequestInit): Promise<T> => {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const headers = {
    "Content-Type": "application/json",
    ...options?.headers,
  };

  let res = await fetch(`${baseUrl}${endpoint}`, {
    credentials: "include",
    ...options,
    headers,
  });

  interface ApiJsonResponse {
    success?: boolean;
    code?: number;
    message?: string;
    errors?: { field?: string; message: string }[];
    [key: string]: unknown;
  }

  let data: ApiJsonResponse;
  try {
    data = await res.clone().json();
  } catch {
    data = {};
  }

  // Access token hết hạn (code 1000) → tự động gọi refresh token
  if (
    res.status === 401 &&
    data?.code === 1000 &&
    !endpoint.includes("/auth/refresh") &&
    !endpoint.includes("/auth/login")
  ) {
    // Chỉ cho 1 request gọi refresh, các request khác chờ cùng 1 promise
    if (!refreshPromise) {
      refreshPromise = refreshToken().finally(() => {
        // Delay clearing to ensure all concurrent waiters get the result
        setTimeout(() => {
          refreshPromise = null;
        }, 100);
      });
    }

    const refreshSuccess = await refreshPromise;

    if (refreshSuccess) {
      // Retry request ban đầu với access_token mới (cookie HTTP-only tự động gửi kèm)
      res = await fetch(`${baseUrl}${endpoint}`, {
        credentials: "include",
        ...options,
        headers,
      });
      data = await res.json();
    } else {
      // Refresh token cũng hết hạn → redirect login
      if (typeof window !== "undefined" && window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
      throw new Error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
    }
  }

  // Refresh token hết hạn (code 1002) → bắt login lại
  if (res.status === 401 && data?.code === 1002 && !endpoint.includes("/auth/login")) {
    if (typeof window !== "undefined" && window.location.pathname !== "/login") {
      window.location.href = "/login";
    }
    throw new Error(data?.message || "Refresh token đã hết hạn, vui lòng đăng nhập lại.");
  }

  if (!res.ok) {
    if (data?.errors && Array.isArray(data.errors) && data.errors.length > 0) {
      const errorMsg = data.errors
        .map((e: { field?: string; message: string }) => `${e.field || "Lỗi"}: ${e.message}`)
        .join("\n");
      throw new Error(errorMsg);
    }
    throw new Error(data?.message || "Có lỗi xảy ra khi gọi API");
  }

  return data as T;
};
