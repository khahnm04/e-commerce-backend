import { sendRequest } from "@/lib/api-client";
import { LoginFormValues } from "@/app/(auth)/login/login.schemas"; // Import type từ schema

export const authApi = {

  login: async (body: LoginFormValues) => {
    return sendRequest<{ success: boolean; message: string; token?: string }>(
      "/auth/login",
      {
        method: "POST",
        body: JSON.stringify(body),
        credentials: "include",
      }
    );
  },

};
