import { sendRequest } from "@/lib/api-client";
import { ApiResponse } from "@/types/common/api.types";

export interface CloudinarySignatureResponse {
    signature: string;
    timestamp: number;
    cloudName: string;
    apiKey: string;
    folder: string;
}

/**
 * Get Cloudinary upload signature from backend
 */
export const getUploadSignature = () =>
    sendRequest<ApiResponse<CloudinarySignatureResponse>>("/upload/signature", {
        method: "GET",
        credentials: "include",
    });

/**
 * Upload file directly to Cloudinary using the signed upload method
 * 1. Get signature from backend
 * 2. Upload to Cloudinary with signature
 * 3. Return the secure_url
 */
export const uploadToCloudinary = async (file: File): Promise<string> => {
    // Step 1: Get signature from backend
    const signatureRes = await getUploadSignature();
    const { signature, timestamp, cloudName, apiKey, folder } = signatureRes.data;

    // Step 2: Build FormData and upload to Cloudinary
    const formData = new FormData();
    formData.append("file", file);
    formData.append("signature", signature);
    formData.append("timestamp", String(timestamp));
    formData.append("api_key", apiKey);
    formData.append("folder", folder);

    const cloudinaryUrl = `https://api.cloudinary.com/v1_1/${cloudName}/image/upload`;

    const res = await fetch(cloudinaryUrl, {
        method: "POST",
        body: formData,
    });

    if (!res.ok) {
        const errorBody = await res.text();
        console.error("Cloudinary upload error:", res.status, errorBody);
        throw new Error("Upload ảnh lên Cloudinary thất bại!");
    }

    const data = await res.json();
    return data.secure_url;
};
