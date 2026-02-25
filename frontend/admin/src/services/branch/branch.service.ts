import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";

interface BranchSimple {
  id: number;
  name: string;
}

const BASE_ENDPOINT = "/admin/branches";

export const branchService = {
  getAll: () =>
    sendRequest<ApiResponse<BranchSimple[]>>(`${BASE_ENDPOINT}?size=100`, {
      method: "GET",
    }),
};
