import { Metadata } from "next";
import RoleForm from "../_components/RoleForm";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { roleService } from "@/services/role.service";

export const metadata: Metadata = {
    title: "Cập nhật nhóm quyền",
    description: "Cập nhật thông tin nhóm quyền",
};

export default async function EditRolePage({
    params,
}: {
    params: Promise<{ slug: string }>;
}) {
    const { slug } = await params;

    let initialData = null;
    try {
        const res = await roleService.getById(slug);
        initialData = res.data;
    } catch (error) {
        console.error("Lỗi khi tải nhóm quyền:", error);
    }

    if (!initialData) {
        return (
            <div className="p-10 text-center">
                <h2 className="text-xl font-bold text-red-500 mb-2">
                    Không tìm thấy nhóm quyền!
                </h2>
                <p className="text-gray-500">
                    Nhóm quyền này có thể đã bị xóa hoặc đường dẫn không đúng.
                </p>
            </div>
        );
    }

    return (
        <>
            <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
                Cập nhật nhóm quyền
            </h1>

            <Breadcrumb
                className="mb-[30px] ml-[5px]"
                items={[
                    { label: "Tổng quan", href: "/" },
                    { label: "Nhóm quyền", href: "/roles" },
                    { label: initialData.name || "Cập nhật" }
                ]}
            />

            <RoleForm initialData={initialData} />
        </>
    )
}
