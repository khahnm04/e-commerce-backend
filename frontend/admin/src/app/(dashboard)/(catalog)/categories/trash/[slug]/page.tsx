"use client";
import { useEffect, useState } from "react";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import CategoryForm from "../../_components/CategoryForm";
import { categoryService } from "@/services/catalog/category.service";
import { CategoryResponse } from "@/types/catalog/category.types";
import { use } from "react";

export default function CategoryTrashDetailPage({
    params,
}: {
    params: Promise<{ slug: string }>;
}) {
    const { slug } = use(params);
    const [data, setData] = useState<CategoryResponse | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const res = await categoryService.getDeletedById(slug);
                setData(res.data);
            } catch {
                console.error("Không thể tải dữ liệu danh mục");
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [slug]);

    if (loading) {
        return (
            <div className="text-center py-10 text-[var(--color-text)]">
                Đang tải dữ liệu...
            </div>
        );
    }

    if (!data) {
        return (
            <div className="p-10 text-center">
                <h2 className="text-xl font-bold text-red-500 mb-2">
                    Không tìm thấy danh mục!
                </h2>
                <p className="text-gray-500">
                    Danh mục này không tồn tại trong thùng rác hoặc đường dẫn không đúng.
                </p>
            </div>
        );
    }

    return (
        <main>
            <h1 className="mt-0 mb-[5px] font-[700] text-[32px] text-[var(--color-text)]">
                Chi tiết danh mục (Thùng rác)
            </h1>

            <Breadcrumb
                className="mb-[30px] ml-[5px]"
                items={[
                    { label: "Tổng quan", href: "/" },
                    { label: "Danh mục", href: "/categories" },
                    { label: "Thùng rác", href: "/categories/trash" },
                    { label: data.name || "Chi tiết" },
                ]}
            />

            <CategoryForm initialData={data} isReadOnly={true} />
        </main>
    );
}
