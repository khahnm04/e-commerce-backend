import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/api.types";
import { Category, CategoryPayload } from "@/types/category.types";

const BASE_ENDPOINT = "/admin/categories";

export const categoryService = {

  /**
   * create category
   */
  create: async (payload: CategoryPayload, imageFile: File) => {
    const formData = new FormData();
    const jsonBlob = new Blob([JSON.stringify(payload)], {
      type: "application/json",
    });
    formData.append("data", jsonBlob);
    formData.append("image", imageFile);
    return sendRequest<ApiResponse<Category>>(`${BASE_ENDPOINT}`, {
      method: "POST",
      body: formData,
      headers: {
        "Content-Type": undefined as any,
      },
    });
  },

  /**
   * update category
   */
  update: async (id: number | string, payload: CategoryPayload, imageFile?: File | null) => {
    const formData = new FormData();
    const jsonBlob = new Blob([JSON.stringify(payload)], {
      type: "application/json",
    });
    formData.append("data", jsonBlob);
    if (imageFile) {
      formData.append("image", imageFile);
    }
    return sendRequest<ApiResponse<Category>>(`${BASE_ENDPOINT}/${id}`, {
      method: "PUT",
      body: formData,
      headers: {
        "Content-Type": undefined as any,
      },
    });
  },

};
