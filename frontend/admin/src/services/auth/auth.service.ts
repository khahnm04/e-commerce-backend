import { sendRequest } from "@/lib/api-client";

export interface LoginBody {
  phoneNumber?: string;
  email?: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  data?: {
    id: number;
    fullName: string;
    avatarUrl: string;
  };
}

export const authApi = {

  login: async (body: LoginBody) => {
    return sendRequest<LoginResponse>(
      "/auth/login",
      {
        method: "POST",
        body: JSON.stringify(body),
        credentials: "include",
      }
    );
  },

  logout: async () => {
    return sendRequest<{ success: boolean; message: string }>(
      "/auth/logout",
      {
        method: "POST",
        credentials: "include",
      }
    );
  },

  refresh: async () => {
    return sendRequest<{ success: boolean; message: string }>(
      "/auth/refresh",
      {
        method: "POST",
        credentials: "include",
      }
    );
  },

};
