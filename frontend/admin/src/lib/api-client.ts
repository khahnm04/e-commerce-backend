let isRefreshing = false;
let refreshPromise: Promise<boolean> | null = null;

const refreshToken = async (): Promise<boolean> => {
  try {
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    const res = await fetch(`${baseUrl}/auth/refresh`, {
      method: "POST",
      credentials: "include",
    });
    return res.ok;
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

  // Nếu access token hết hạn (401), tự động gọi refresh token
  if (res.status === 401 && !endpoint.includes("/auth/refresh") && !endpoint.includes("/auth/login")) {
    // Tránh gọi refresh nhiều lần cùng lúc
    if (!isRefreshing) {
      isRefreshing = true;
      refreshPromise = refreshToken();
    }

    const refreshSuccess = await refreshPromise;
    isRefreshing = false;
    refreshPromise = null;

    if (refreshSuccess) {
      // Gọi lại request ban đầu với token mới
      res = await fetch(`${baseUrl}${endpoint}`, {
        ...options,
        headers,
      });
    } else {
      // Refresh thất bại → chuyển hướng về trang login
      if (typeof window !== "undefined") {
        window.location.href = "/login";
      }
      throw new Error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
    }
  }

  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.message || "Có lỗi xảy ra khi gọi API");
  }

  return data as T;
};
