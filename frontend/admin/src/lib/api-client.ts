import { authApi } from "@/services/auth/auth.service";

let isRefreshing = false;
let refreshPromise: Promise<boolean> | null = null;

const refreshToken = async (): Promise<boolean> => {
  try {
    await authApi.refresh();
    return true;
  } catch {
    return false;
  }
};

export const sendRequest = async <T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> => {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const headers = {
    "Content-Type": "application/json",
    ...options?.headers,
  };

  let res = await fetch(`${baseUrl}${endpoint}`, {
    ...options,
    headers,
  });

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  let data: any;
  try {
    data = await res.clone().json();
  } catch {
    data = {};
  }

  // Về Access token: Nếu lỗi là 1000 (Unauthenticated), tự động gọi refresh token
  if (res.status === 401 && data?.code === 1000 && !endpoint.includes("/auth/refresh") && !endpoint.includes("/auth/login")) {
    if (!isRefreshing) {
      isRefreshing = true;
      refreshPromise = refreshToken();
    }

    const refreshSuccess = await refreshPromise;
    isRefreshing = false;
    refreshPromise = null;

    if (refreshSuccess) {
      // Gọi lại request ban đầu với cookie HTTP-only mới (sẽ được tự động gửi kèm fetch credentials)
      res = await fetch(`${baseUrl}${endpoint}`, {
        ...options,
        headers,
      });
      data = await res.json();
    } else {
      if (typeof window !== "undefined" && window.location.pathname !== "/login") {
        window.location.href = "/login";
      }
      throw new Error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
    }
  }

  // Về Refresh token: Nếu lỗi là 1002, bắt người dùng login lại
  if (res.status === 401 && data?.code === 1002 && !endpoint.includes("/auth/login")) {
    if (typeof window !== "undefined" && window.location.pathname !== "/login") {
      window.location.href = "/login";
    }
    throw new Error(data?.message || "Refresh token đã hết hạn, vui lòng đăng nhập lại.");
  }

  if (!res.ok) {
    throw new Error(data?.message || "Có lỗi xảy ra khi gọi API");
  }

  return data as T;
};
